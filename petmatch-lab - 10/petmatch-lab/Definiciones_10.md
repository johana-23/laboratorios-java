# Laboratorio 10 — PetMatch Mini

## 1. Conceptos principales

- **Clave primaria (PK):** identifica de forma única cada registro. En `pets` es `id`; en `support_requests` también es `id`.
- **Clave foránea (FK):** referencia la clave primaria de otra tabla. En este caso, `support_requests.pet_id` referencia `pets.id`.
- **Uno a muchos (1:N):** una mascota puede tener cero, una o varias solicitudes.
- **Muchos a uno (N:1):** cada solicitud pertenece a una sola mascota.
- **Entidad JPA:** clase Java que representa información persistente. `Pet` y `SupportRequest` son entidades.
- **Enum:** tipo Java con un conjunto limitado de valores. `SupportType` representa el tipo de apoyo y `RequestStatus` representa el estado.
- **DTO:** objeto que recibe los datos del formulario. `SupportRequestForm` solo recibe tipo y descripción; no recibe el id, la mascota ni el estado.
- **Repositorio:** interfaz que consulta y persiste entidades. `SupportRequestRepository` concentra las consultas de solicitudes.
- **Servicio:** coordina las operaciones y aplica reglas de negocio.
- **Transacción:** agrupa cambios para confirmarlos juntos o revertirlos ante un fallo.
- **Commit:** confirma los cambios de una transacción.
- **Rollback:** revierte los cambios no confirmados de una transacción.
- **Carga LAZY:** estrategia que evita cargar inmediatamente la mascota asociada cuando se consulta una solicitud.
- **HTTP 404:** el recurso solicitado no existe dentro del contexto indicado.
- **HTTP 409:** el recurso existe, pero una regla impide realizar la operación.

## 2. Diagnóstico inicial

1. El identificador es distinto del nombre porque el nombre puede repetirse o cambiar; el id identifica el registro de manera estable.
2. Se evita crear una solicitud para una mascota inexistente recuperando la mascota mediante `PetRepository` antes de construir `SupportRequest`.
3. La relación se conserva porque la solicitud referencia el id de la mascota y no su nombre.
4. La validación del formulario comprueba los datos recibidos; el servicio comprueba además si la operación está permitida por las reglas del sistema.
5. Si una operación modifica dos filas y falla la segunda, se espera que la transacción revierta también el primer cambio.
6. La eliminación es delicada porque otras filas pueden referenciar la mascota. En este laboratorio se conserva el historial y se bloquea su eliminación.

## 3. Práctica 10.1 — Modelo y reglas

### Modelo relacional

| Tabla | Clave primaria | Columnas principales |
|---|---|---|
| `pets` | `id` | `name`, `species`, `age`, `description` |
| `support_requests` | `id` | `pet_id`, `type`, `description`, `status` |

Ejemplo de relación:

| Mascota | Solicitudes |
|---|---|
| Luna (`pets.id = 1`) | Paseo (`id = 10`), Alimentación (`id = 11`) |
| Milo (`pets.id = 2`) | Compañía (`id = 12`) |
| Kira (`pets.id = 3`) | Sin solicitudes |

`pet_id` está en `support_requests` porque cada solicitud pertenece a una mascota. Una solicitud no puede pertenecer simultáneamente a dos mascotas, por eso tiene una sola columna `pet_id`.

### Reglas del dominio

1. Cada solicitud debe pertenecer a una mascota existente.
2. En el flujo normal de la aplicación, una mascota no puede tener más de una solicitud abierta.
3. Una solicitud nueva comienza en estado `OPEN`.
4. Una solicitud abierta puede completarse o cancelarse.
5. Una solicitud completada o cancelada no puede cambiar nuevamente de estado.
6. Una mascota que tenga solicitudes históricas no puede eliminarse, aunque todas estén finalizadas.

### Estados y transiciones

| Estado actual | Acción | Resultado |
|---|---|---|
| `OPEN` | Completar | `COMPLETED` |
| `OPEN` | Cancelar | `CANCELLED` |
| `COMPLETED` | Completar o cancelar | Operación no permitida |
| `CANCELLED` | Completar o cancelar | Operación no permitida |

### Ejercicio y depuración

- Las claves primarias son `pets.id` y `support_requests.id`.
- La clave foránea es `support_requests.pet_id`.
- La solicitud tiene un solo `pet_id` porque pertenece a una sola mascota.
- El estado de la solicitud no se modifica desde el formulario de mascota; se cambia mediante las operaciones de solicitudes.
- No se elimina una mascota con historial porque se perdería la referencia y se incumpliría la regla de conservación.
- Usar el nombre como clave foránea sería inseguro porque puede repetirse o cambiar.
- Copiar los datos completos de la mascota en cada solicitud produciría información duplicada y posiblemente desactualizada.
- El borrado en cascada no se utiliza porque eliminaría el historial que debe conservarse.

## 4. Práctica 10.2 — Relación JPA

En `SupportRequest` se utiliza:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "pet_id", nullable = false)
private Pet pet;
```

`@ManyToOne` indica que varias solicitudes pueden apuntar a una misma mascota. `@JoinColumn` indica que la columna de unión se llama `pet_id`. `nullable = false` y `optional = false` expresan que la relación es obligatoria.

No se agregó `@OneToMany` a `Pet` porque la consulta de solicitudes se realiza directamente mediante el repositorio. El modelo usa una asociación unidireccional para evitar una colección adicional y mantener las vistas simples.

Los enums se guardan con `@Enumerated(EnumType.STRING)`, de modo que la base almacena valores como `OPEN` y `WALK`, mientras que la interfaz presenta etiquetas como “Abierta” y “Paseo”.

No se configura borrado en cascada ni `orphanRemoval`, ya que las solicitudes son parte del historial.

### Comprobación en MariaDB

```sql
USE petmatch_lab;

SHOW TABLES;
DESCRIBE support_requests;
SHOW CREATE TABLE support_requests;
```

La tabla debe contener `id`, `pet_id`, `type`, `description` y `status`; además, `pet_id` debe referenciar `pets.id`.

## 5. Práctica 10.3 — Repositorio

`SupportRequestRepository` incluye estas consultas derivadas:

- `findByPet_IdOrderByIdDesc(petId)`: devuelve las solicitudes de una mascota, ordenadas por id descendente.
- `findByIdAndPet_Id(id, petId)`: busca una solicitud y verifica que pertenezca a la mascota indicada.
- `existsByPet_Id(petId)`: comprueba si la mascota tiene solicitudes.
- `existsByPet_IdAndStatus(petId, status)`: comprueba si existe una solicitud con el estado indicado.

El guion bajo de `Pet_Id` permite recorrer las propiedades Java `SupportRequest.pet.id`. El repositorio responde consultas; el servicio decide qué significan esas respuestas para las reglas de negocio.

La consulta de verificación en MariaDB es:

```sql
SELECT
    r.id,
    r.pet_id,
    p.name AS pet_name,
    r.type,
    r.status
FROM support_requests AS r
INNER JOIN pets AS p ON p.id = r.pet_id
ORDER BY r.pet_id, r.id DESC;
```

## 6. Práctica 10.4 — Formularios y reglas

`SupportRequestForm` recibe únicamente `type` y `description`. El id se genera al guardar, la mascota se recupera desde la ruta y el estado se asigna desde la entidad al crear la solicitud.

El servicio ejecuta el siguiente flujo:

1. Valida los datos básicos del formulario.
2. Busca la mascota indicada.
3. Comprueba que no exista otra solicitud `OPEN`.
4. Construye una solicitud con estado inicial `OPEN`.
5. Guarda la entidad mediante `SupportRequestRepository`.

La validación de campos se realiza con Bean Validation (`@NotNull`, `@NotBlank` y `@Size`). Las reglas como “una sola solicitud abierta” se comprueban en el servicio. Las transiciones se protegen dentro de la entidad con `complete()` y `cancel()`.

### Clasificación de situaciones

| Situación | Responsable | Resultado esperado |
|---|---|---|
| Descripción vacía o con espacios | Validación del formulario | Se vuelve a mostrar el formulario con errores |
| Mascota inexistente | Servicio y excepción de recurso | 404 |
| Completar una solicitud cancelada | Entidad y regla de negocio | 409 |
| Crear una segunda solicitud abierta | Servicio y repositorio | Mensaje de regla, sin insertar |
| Consultar una solicitud que pertenece a otra mascota | Consulta `findByIdAndPet_Id` | 404 |

### Verificación de datos

```sql
SELECT id, pet_id, type, description, status
FROM support_requests
ORDER BY pet_id, id DESC;
```

Al completar o cancelar una solicitud se conserva su id y su `pet_id`; cambia el estado.

## 7. Práctica 10.5 — Transacciones y reemplazo

Reemplazar una solicitud consiste en cancelar la anterior y crear una nueva. Ambos cambios se realizan dentro de `SupportRequestService.replace(...)`, anotado con `@Transactional`.

Si la segunda operación falla y provoca rollback, el cambio de estado de la solicitud original también se revierte. La solicitud anterior permanece `OPEN` y no queda creada una solicitud nueva.

No se llama a `save(previous)` porque la solicitud anterior se recupera dentro de la transacción y es una entidad administrada por JPA. Hibernate detecta el cambio de estado. La solicitud nueva sí se guarda mediante el repositorio.

`readOnly = true` comunica que un método está pensado para lectura y puede permitir optimizaciones; no es un mecanismo universal de permisos ni sustituye las reglas de negocio.

### Escenarios del ejercicio

1. Reemplazo válido: la original queda `CANCELLED` y se crea una nueva `OPEN`.
2. Reemplazo de una solicitud `COMPLETED`: se rechaza; no se crea otra solicitud.
3. Formulario con descripción vacía: se muestran errores y no se modifica la solicitud original.
4. Solicitud perteneciente a otra mascota: no se encuentra dentro de esa ruta y se responde 404.
5. Fallo de persistencia antes del commit: se revierte la cancelación y no queda un reemplazo parcial.

La comprobación de “no existe solicitud abierta” resuelve el flujo secuencial del laboratorio, pero dos solicitudes simultáneas podrían superar la comprobación al mismo tiempo. `@Transactional` por sí solo no garantiza exclusividad global; en un sistema con concurrencia alta se necesitaría una estrategia adicional de bloqueo o restricción en la base de datos.

## 8. Práctica 10.6 — Excepciones y pruebas

`ResourceNotFoundException` se traduce a HTTP 404 y `BusinessRuleException` a HTTP 409 mediante `PageErrorHandler`. La página `errors/status.html` presenta el mensaje. No se captura cualquier excepción, porque los fallos inesperados deben seguir siendo visibles para poder diagnosticarlos.

La eliminación de mascotas consulta `existsByPet_Id`. Si hay solicitudes, el servicio lanza `BusinessRuleException`; si no existen solicitudes, conserva el comportamiento de eliminación del CRUD anterior.

### Grupos de pruebas

- **Entidad:** verifica el estado inicial y las transiciones permitidas o rechazadas.
- **Repositorio JPA:** comprueba que la relación se persiste y que las consultas derivadas filtran por mascota.
- **Servicio:** comprueba las reglas con repositorios simulados, sin iniciar MariaDB.
- **Transacción:** verifica el reemplazo y que un fallo dentro de la transacción provoque rollback.
- **Controlador MVC:** comprueba las respuestas HTTP y las vistas de 404, 409 y los formularios inválidos.
- **CRUD de mascotas:** conserva las pruebas anteriores y comprueba que no se pueda eliminar una mascota con solicitudes.

Las pruebas usan H2 configurada en `src/test/resources/application.yaml`. H2 permite probar las capas Java/JPA en un esquema temporal, pero no garantiza que cada detalle de comportamiento sea idéntico en MariaDB.

## 9. Reto integrador: verificación manual en MariaDB

1. Registra una solicitud de paseo desde la interfaz.
2. Consulta `support_requests` y verifica que `pet_id` corresponda al id de la mascota.
3. Intenta registrar una segunda solicitud abierta para esa mascota; debe mostrarse el conflicto y no debe insertarse otra fila.
4. Reemplaza la solicitud abierta con otra solicitud.
5. Comprueba que la original quedó `CANCELLED` y la nueva `OPEN`.
6. Completa la nueva solicitud e intenta completarla otra vez; la segunda operación debe responder 409.
7. Intenta eliminar la mascota; debe responder 409 y conservar el registro.
8. Reinicia la aplicación y confirma que el historial continúa disponible.
9. Ejecuta las pruebas automatizadas y revisa los resultados.

No se deben modificar manualmente las filas de `support_requests` para completar este flujo. Las operaciones se hacen desde la aplicación y las consultas SQL se utilizan para comprobar sus resultados.

## 10. Diagnóstico final

1. `pet_id` está en `support_requests` porque cada solicitud pertenece a una mascota y puede haber muchas solicitudes por mascota.
2. `@ManyToOne` representa la cardinalidad de la asociación; `@JoinColumn` define la columna que almacena la clave foránea.
3. `@OneToMany(mappedBy = "pet")` permitiría recorrer solicitudes desde `Pet`, pero no es imprescindible porque el repositorio puede consultarlas por `pet.id`.
4. `SupportRequestForm` no contiene `status` porque el usuario no debe decidir el estado inicial ni saltarse las transiciones.
5. `@NotBlank` valida el contenido del campo; “solo una solicitud abierta” es una regla que necesita consultar el estado persistido.
6. `findByIdAndPet_Id(...)` garantiza que la solicitud pertenece a la mascota indicada en la ruta.
7. Se impide eliminar una mascota con solicitudes para conservar su historial y evitar referencias inválidas.
8. La segunda llamada a `complete()` lanza `BusinessRuleException`, porque la solicitud ya no está abierta.
9. `replace(...)` usa `@Transactional` porque cancela una solicitud e inserta otra como una sola operación lógica.
10. Si la segunda parte falla y la excepción provoca rollback, el primer cambio también se revierte.
11. `readOnly = true` expresa la intención de lectura y puede optimizar el acceso; no debe interpretarse como un sistema de permisos.
12. 404 indica que no existe el recurso; 409 indica que existe, pero la operación no está permitida.
13. Una prueba unitaria verifica lógica aislada, normalmente con dependencias simuladas. Una prueba de repositorio verifica consultas y mapeos JPA contra una base de pruebas.
14. H2 y MariaDB son motores distintos; pueden diferir en tipos, restricciones, SQL y comportamiento específico.
15. Dos personas podrían comprobar simultáneamente que no hay solicitud abierta y crear ambas una solicitud. Para evitarlo en escenarios concurrentes se requiere una estrategia adicional de integridad o bloqueo.

## 11. Comandos para ejecutar

En PowerShell, desde la carpeta raíz:

```powershell
$env:DB_URL = 'jdbc:mariadb://127.0.0.1:3306/petmatch_lab'
$env:DB_USERNAME = 'petmatch_user'
$env:DB_PASSWORD = 'TU_CLAVE_LOCAL'

.\mvnw.cmd spring-boot:run
```

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```

La aplicación normal utiliza MariaDB. Las pruebas automatizadas utilizan H2.
