# Solución y desarrollo — Laboratorio 9

**PetMatch Mini — Persistencia con MariaDB, JPA, Hibernate y Spring Data JPA**  
**Programa:** Análisis y Desarrollo de Software (ADSO) — SENA


# Diagnóstico inicial

1. **¿Dónde guarda los objetos `InMemoryPetRepository`?** En un `Map<Long, Pet>` llamado `pets`, ubicado en la memoria del proceso Java.
2. **¿Qué sucede cuando termina Java?** Los objetos del `Map` dejan de existir. Al iniciar otra vez, el constructor recreaba los datos de ejemplo; los registros nuevos no quedaban guardados.
3. **¿Por qué el formulario no debe conocer la contraseña de la base de datos?** Porque el formulario se muestra en el navegador. La contraseña es un secreto del servidor y debe permanecer en el entorno donde se ejecuta Spring Boot.
4. **¿PetService debe saber si los datos están en un Map o en MariaDB?** No debería depender del mecanismo concreto. Debe utilizar el contrato de un repositorio, así se puede cambiar el almacenamiento sin reescribir el controlador.
5. **Diferencia entre objeto Java y fila:** el objeto es una instancia de una clase con atributos y métodos; la fila es un registro de una tabla con valores en columnas.
6. **¿Cómo demostrar persistencia?** Registrar una mascota, consultar la fila mediante SQL, detener Spring Boot sin detener MariaDB, volver a consultar la tabla y luego iniciar la aplicación para comprobar que el registro sigue disponible.

# Práctica 9.1 — De la memoria de Java a una base de datos relacional

## Conceptos

- **Persistencia:** capacidad de conservar información para recuperarla después de finalizar el proceso que la creó.
- **Proceso Java:** programa de la aplicación que se está ejecutando. Sus objetos viven en la memoria del proceso.
- **Base de datos:** conjunto organizado de información, por ejemplo `petmatch_lab`.
- **Servidor MariaDB:** programa que administra la base de datos, recibe consultas y almacena registros.
- **Cliente MariaDB:** herramienta que permite conectarse al servidor y ejecutar instrucciones SQL.
- **Tabla:** estructura que agrupa registros de un mismo tipo; en este laboratorio se utiliza `pets`.
- **Columna:** dato definido para cada registro, por ejemplo `name` o `age`.
- **Fila:** registro individual de una mascota.
- **Clave primaria:** columna o conjunto de columnas que identifica de manera única cada fila. En `pets` es `id`.
- **Restricción:** regla que debe cumplir un dato almacenado, como `NOT NULL`.
- **SQL:** lenguaje utilizado para consultar y modificar información en bases de datos relacionales.
- **NULL:** indica que no hay un valor almacenado. No equivale a una cadena vacía (`''`) ni a espacios (`'   '`).
- **Relación:** vínculo entre tablas, normalmente mediante una clave foránea. En este ejercicio se trabaja primero con una sola tabla.

## Traducción de consultas

```sql
SELECT name, age
FROM pets
WHERE age >= 3
ORDER BY age DESC;
```

Muestra el nombre y la edad de las mascotas que tienen tres años o más, ordenando primero las de mayor edad.

```sql
SELECT COUNT(*) FROM pets;
```

Cuenta cuántas filas existen en `pets`.

Respuestas:
1. La primera consulta no modifica filas; solamente lee información porque utiliza `SELECT`.
2. `COUNT(*)` cuenta todas las filas que cumplen las condiciones. Como no hay `WHERE`, cuenta todas las mascotas.
3. Se necesita la columna `id`.
4. No. Dos filas pueden tener el mismo nombre; por eso cada registro tiene una clave primaria diferente.

## Depuración y comprobación

- **La tabla no existe:** comprobar la base seleccionada con `SELECT DATABASE()` y revisar `SHOW TABLES;`.
- **Aparecen filas de otra aplicación:** revisar qué base está seleccionada y cuál utiliza la URL JDBC.
- **La consulta devuelve cero filas:** puede que no existan registros o que el filtro `WHERE` no coincida con los valores guardados.
- **Un identificador parece duplicarse:** revisar que `id` esté definido como clave primaria.

Un `Map` pertenece a la memoria del proceso Java, mientras que una tabla MariaDB conserva registros en el almacenamiento administrado por el servidor. Las operaciones CRUD se relacionan con SQL así: Create–`INSERT`, Read–`SELECT`, Update–`UPDATE` y Delete–`DELETE`.

# Práctica 9.2 — Preparar MariaDB y conectar Spring Boot

## Conceptos

- **JDBC (Java Database Connectivity):** API de Java para comunicarse con bases de datos relacionales.
- **Driver JDBC:** biblioteca que implementa la comunicación con un motor específico. Se utiliza MariaDB Connector/J.
- **DataSource:** objeto que permite a Spring obtener conexiones a la base de datos; normalmente administra un conjunto de conexiones reutilizables.
- **URL JDBC:** dirección que indica motor, host, puerto y base, por ejemplo `jdbc:mariadb://127.0.0.1:3306/petmatch_lab`.
- **Variable de entorno:** valor definido en el entorno del proceso. Se usa para proporcionar credenciales sin escribirlas en el código.
- **Maven:** herramienta que administra dependencias y tareas de construcción del proyecto Java.
- **`ddl-auto`:** propiedad que indica cómo Hibernate gestiona el esquema. `update` se usa como apoyo local, no como sistema de migraciones.

## Clasificación de problemas

| Caso | Tipo | Explicación |
|---|---|---|
| 1. `mariadb-java-client` no puede descargarse | Maven | Problema de conexión al repositorio de dependencias o de resolución de versión. |
| 2. `DB_PASSWORD` no está definida | Configuración | Spring no recibe la variable de entorno requerida. |
| 3. El servidor escucha en otro puerto | Conectividad | La URL JDBC apunta a un puerto donde MariaDB no escucha. |
| 4. El usuario no tiene permisos | Autenticación/autorización | Las credenciales pueden ser correctas, pero la cuenta no tiene permisos sobre `petmatch_lab`. |
| 5. Nombre de base mal escrito en la URL | Configuración | La aplicación intenta conectarse a una base diferente o inexistente. |
| 6. `spring.datasource.username` mal indentada | Configuración | YAML interpreta las propiedades según la indentación; una jerarquía incorrecta impide leer el valor esperado. |

## Preparación de la base de datos

Ejecutar en una sesión administrativa de MariaDB, adaptando la contraseña a una clave local propia:

```sql
CREATE DATABASE IF NOT EXISTS petmatch_lab
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'petmatch_user'@'127.0.0.1'
  IDENTIFIED BY 'CAMBIAR_POR_UNA_CLAVE_LOCAL_SEGURA';

GRANT ALL PRIVILEGES ON petmatch_lab.*
  TO 'petmatch_user'@'127.0.0.1';

SHOW GRANTS FOR 'petmatch_user'@'127.0.0.1';
```

Luego probar la conexión:

```bash
mariadb --protocol=TCP -h 127.0.0.1 -P 3306 -u petmatch_user -p petmatch_lab
```

Dentro del cliente ejecutar `SELECT VERSION();`, `SELECT DATABASE();` y `SHOW TABLES;`. No se incluye una contraseña real en el ZIP.

**Windows PowerShell:**

```powershell
$env:DB_URL = 'jdbc:mariadb://127.0.0.1:3306/petmatch_lab'
$env:DB_USERNAME = 'petmatch_user'
$env:DB_PASSWORD = 'LA_CLAVE_LOCAL_QUE_CONFIGURASTE'
```

La URL utiliza `127.0.0.1` y el puerto `3306` . Si MariaDB utiliza otro puerto, se debe ajustar `DB_URL` al puerto real.

`src/main/resources/application.yaml` lee `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` desde el entorno. No contiene contraseñas. Se conserva `ddl-auto: update`, `open-in-view: false` y el registro SQL para las comprobaciones.

# Práctica 9.3 — Convertir Pet en una entidad JPA

## Conceptos

- **ORM (Object-Relational Mapping):** técnica que relaciona objetos Java con tablas relacionales.
- **Jakarta Persistence (JPA):** especificación que define anotaciones y contratos para mapear entidades Java.
- **Hibernate:** implementación ORM que gestiona entidades y genera SQL.
- **Spring Data JPA:** módulo de Spring que simplifica el acceso a datos mediante interfaces de repositorio.
- **Entidad:** clase Java administrada por JPA y asociada con una tabla.
- **`@Entity`:** declara que la clase es una entidad JPA.
- **`@Table`:** permite indicar el nombre de la tabla.
- **`@Id`:** marca el identificador de la entidad.
- **`@GeneratedValue`:** indica que el identificador será generado según la estrategia configurada.
- **`GenerationType.IDENTITY`:** utiliza una columna de identidad generada por la base de datos, como `AUTO_INCREMENT` en MariaDB.
- **`@Column`:** define detalles del mapeo, como longitud y posibilidad de `NULL`.

## Ejercicio — Reconocer responsabilidades en Pet.java

1. Nombre de tabla: `@Table(name = "pets")`.
2. Propiedad que identifica la fila: `id`, marcada con `@Id`.
3. Estrategia del identificador: `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
4. Que `description` pueda estar ausente: su columna no tiene `nullable = false`, por lo que puede aceptar `NULL`.
5. Longitud del nombre: `@Column(nullable = false, length = 100)`.
6. Constructor para crear una mascota: `Pet(String name, String species, int age, String description)`.
7. Método que cambia datos editables: `updateDetails(...)`.

Si se elimina `getName()`, Thymeleaf no tendrá el getter esperado para resolver `${pet.name}`. Si se elimina `getId()`, ocurrirá lo mismo con `${pet.id}`. Por eso se conservaron los getters.

## Preguntas y errores

- **¿Por qué el constructor vacío?** JPA necesita crear instancias al recuperar filas. Se requiere un constructor sin argumentos `public` o `protected`; el proyecto utiliza `protected Pet()`.
- **¿Por qué `Long` y no `long`?** `Long` puede ser `null`, que representa que una entidad nueva todavía no tiene identificador. `long` es primitivo y su valor inicial sería `0`.
- **¿Qué pasa si se elimina `@Entity`?** Hibernate no reconoce `Pet` como entidad administrada y el repositorio no puede persistirla correctamente.
- **¿Qué pasa si se cambia el ID a `String` manteniendo `IDENTITY`?** Se pierde la correspondencia con la estrategia de identidad numérica que utiliza la columna autogenerada.
- **¿Qué pasa si se elimina el constructor vacío?** Se incumple el requisito que JPA necesita para instanciar la entidad al recuperarla.
- **¿Por qué no crear `new Pet()` sin datos?** Podría producir un objeto sin nombre, especie ni edad válida. La creación normal usa el constructor con datos y el servicio valida antes de guardar.

La restricción `nullable = false` evita que se guarde SQL `NULL`, pero no reemplaza `@NotBlank` ni impide por sí sola que una edad sea negativa; la validación del formulario y las reglas del servicio siguen siendo necesarias.

Se reemplazó `model/Pet.java` con una entidad que utiliza `@Entity`, `@Table`, `@Id`, `@GeneratedValue` y `@Column`. El ID ahora es `Long` y lo genera MariaDB al persistir. Se mantuvieron los getters y se agregó `updateDetails(...)` para cambiar los campos editables sin modificar el ID.

# Práctica 9.4 — Sustituir el repositorio en memoria por Spring Data JPA

## Conceptos

- **Repositorio:** componente encargado de acceder a los datos, separado de la lógica del servicio.
- **`JpaRepository<Pet, Long>`:** interfaz de Spring Data JPA para operaciones sobre entidades `Pet` cuyo identificador es `Long`.
- **`findAll()`:** consulta todas las entidades.
- **`findById(id)`:** busca por identificador y devuelve `Optional<Pet>`.
- **`save(pet)`:** guarda una entidad nueva o los cambios de una existente.
- **`delete(pet)`:** solicita eliminar una entidad.
- **`existsById(id)`:** verifica si existe un registro con ese ID.
- **`count()`:** cuenta los registros.
- **`Optional<Pet>`:** representa que una búsqueda puede contener una mascota o estar vacía.
- **Entidad transient:** objeto creado en Java que todavía no se ha guardado.
- **Entidad managed:** entidad cuyo estado sigue el contexto de persistencia.
- **Entidad detached:** entidad persistida que ya no pertenece al contexto de persistencia activo.
- **Contexto de persistencia:** mecanismo de JPA que mantiene el seguimiento de las entidades durante una unidad de trabajo.

## Ejercicio — Identificar el origen de cada comportamiento

1. Mostrar `pets/form` si hay errores: **PetController**.
2. Ofrecer `findById(...)`: **PetRepository**, por herencia de `JpaRepository`.
3. Generar el valor de la columna autoincremental: **MariaDB**, mediante la columna de identidad.
4. Quitar espacios al inicio y final del nombre: **PetService**, usando `strip()`.
5. Convertir una búsqueda inexistente en HTTP 404: **PetController**, al interpretar el `Optional` vacío.
6. Ejecutar la operación final de almacenamiento: **Hibernate/JPA**, invocado mediante la implementación generada por Spring Data.

Se creó `repository/PetRepository.java` como interfaz que extiende `JpaRepository<Pet, Long>`. Spring Data JPA genera la implementación al iniciar el contexto; por eso no se creó una clase `PetRepositoryImpl` ni se escribieron sentencias SQL para el CRUD básico. Los métodos `findAll`, `findById`, `save` y `delete` provienen de la interfaz heredada. MariaDB genera el identificador mediante la columna de identidad. Se eliminó `InMemoryPetRepository.java`, porque el `Map` ya no se utiliza. El controlador continúa dependiendo únicamente de `PetService`; `PetForm` y las plantillas siguen representando los datos de entrada y presentación.

# Práctica 9.5 — Persistir el CRUD y comprobar los cambios

## Contrato del servicio

Se conservaron los métodos que necesita el controlador:

| Método | Resultado |
|---|---|
| `findAll()` | `List<Pet>` ordenada por ID ascendente |
| `findById(long id)` | `Optional<Pet>` |
| `create(PetForm form)` | `Pet` guardada |
| `update(long id, PetForm form)` | `Optional<Pet>` |
| `delete(long id)` | `boolean` |

La implementación ahora utiliza `PetRepository`. `create` construye una entidad sin ID y llama a `save`. `update` busca la entidad existente, actualiza sus campos y llama a `save`. `delete` busca la entidad y la elimina; si no existe, devuelve `false`.

## Consultas SQL para comprobaciones manuales

Después de iniciar la aplicación y registrar una mascota desde el formulario:

```sql
SHOW TABLES;
DESCRIBE pets;
SHOW CREATE TABLE pets;

SELECT id, name, species, age, description
FROM pets
ORDER BY id;
```

Para revisar una edición, reemplazar el ID por el identificador real observado en el navegador:

```sql
SELECT id, name, age
FROM pets
WHERE id = 7;
```

Para revisar una eliminación:

```sql
SELECT id, name, species, age
FROM pets
ORDER BY id;
```

Consultas adicionales:

```sql
SELECT COUNT(*) AS total_pets FROM pets;

SELECT DISTINCT species FROM pets ORDER BY species;

SELECT id, name, age
FROM pets
WHERE age >= 3
ORDER BY age DESC, name ASC;

SELECT id, name, species
FROM pets
WHERE name LIKE '%a%'
ORDER BY name;
```

Estas sentencias son consultas de lectura. No se deben ejecutar ejemplos `UPDATE` o `DELETE` sin confirmar antes el filtro `WHERE` y el registro que se desea modificar.

## Ejercicio — El identificador no es un contador

1. Las mascotas que no se eliminaron deben permanecer guardadas en la tabla.
2. No. El ID nuevo no tiene que ser el siguiente número sin saltos; los identificadores generados pueden dejar espacios después de eliminaciones.
3. No se debe mostrar el total usando el ID más alto, porque puede haber saltos y registros eliminados.
4. La consulta correcta para contar filas es `SELECT COUNT(*) FROM pets;`.
5. La decisión de qué registro eliminar se coordina desde el controlador y el servicio; el repositorio ejecuta la operación de persistencia solicitada. La vista solo envía la petición para el registro elegido.

## Reto — Consulta derivada por especie

Se añadió al repositorio el método:

```java
List<Pet> findBySpeciesIgnoreCaseOrderByNameAsc(String species);
```

`findBy` indica búsqueda; `Species` corresponde a la propiedad `species`; `IgnoreCase` ignora diferencias entre mayúsculas y minúsculas; `OrderByNameAsc` ordena por nombre ascendente. El servicio conserva `findBySpecies(...)`: si no recibe especie devuelve todas las mascotas y, si recibe una especie, llama al método derivado. Se añadió una prueba JPA que guarda mascotas de distintas especies y comprueba el filtro y el orden.

## Depuración de 9.5

- **No compila por referencia a `InMemoryPetRepository`:** buscar imports y pruebas que todavía utilizan la clase eliminada.
- **Error JDBC al iniciar:** comprobar servidor, URL, puerto, usuario, contraseña y permisos.
- **No aparece la tabla:** comprobar `@Entity`, `@Table`, el paquete escaneado, la base seleccionada y `ddl-auto`.
- **Errores de edad:** revisar validación del formulario y reglas del servicio.
- **Catálogo vacío inicialmente:** es normal si la tabla recién creada no tiene filas; los datos del antiguo `Map` no se migran automáticamente.
- **Datos desaparecen al reiniciar:** revisar que la aplicación use el repositorio JPA y que se conecte siempre a la misma base.
- **Datos existen en SQL pero no en HTML:** seguir el recorrido repositorio → servicio → controlador/Model → Thymeleaf.
- **Edición no guardada:** revisar que se recupere la entidad y que el servicio llame a `save(...)`.
- **Datos inesperados:** comprobar `SELECT DATABASE()` y el valor de `DB_URL`.

# Práctica 9.6 — Pruebas, depuración y reto integrador

## Tipos de pruebas

- **Prueba unitaria del servicio:** utiliza `PetService` real y un `PetRepository` simulado con Mockito. Comprueba reglas, normalización y colaboración sin conectarse a MariaDB.
- **Prueba de integración JPA:** utiliza el repositorio real, Hibernate y una base H2 de prueba. Comprueba el mapeo y operaciones de persistencia en ese entorno temporal.
- **Prueba de integración MVC:** inicia el contexto de Spring y utiliza MockMvc para comprobar rutas, formularios, validación y respuestas.
- **Comprobación manual en MariaDB:** demuestra que la aplicación real guarda los datos en MariaDB y que permanecen después de reiniciar Spring Boot.

Una prueba en H2 no demuestra por sí sola la persistencia en MariaDB porque H2 es una base de datos diferente, temporal y configurada solo para pruebas. `saveAndFlush(...)` guarda y sincroniza los cambios con H2; `EntityManager.clear()` limpia las entidades administradas para que la consulta posterior recupere los datos desde la base de prueba y no reutilice simplemente el mismo objeto en memoria. En las pruebas Mockito, `thenAnswer(...)` devuelve el objeto recibido por `save(...)`; sirve para comprobar la normalización del servicio, pero no simula la generación de ID de MariaDB.

## Configuración de pruebas

Se agregaron `spring-boot-starter-data-jpa-test`, `h2` y `mockito-junit-jupiter` con alcance `test`. Se creó `src/test/resources/application.yaml` para usar H2 en memoria con `MODE=MariaDB` y `ddl-auto: create-drop`. Esta configuración es exclusiva de pruebas y no reemplaza la configuración de MariaDB de `src/main/resources/application.yaml`.

## Pruebas de repositorio

Se eliminó `InMemoryPetRepositoryTests.java` y se creó `PetRepositoryTests.java` con `@DataJpaTest`. Las pruebas comprueban:

1. Que al guardar una entidad nueva se genera un ID.
2. Que se recupera una mascota por su identificador.
3. Que se persisten los cambios de una mascota existente.
4. Que se elimina una mascota guardada.
5. Que una búsqueda por ID inexistente devuelve un `Optional` vacío.
6. Que la búsqueda derivada por especie ignora mayúsculas/minúsculas y ordena por nombre.

## Pruebas del servicio

Se reemplazó la prueba que creaba `InMemoryPetRepository` por una prueba con Mockito. El repositorio es un mock y se verifica que:

1. El servicio elimina espacios innecesarios antes de guardar.
2. Una edad negativa se rechaza antes de llamar a `save`.
3. Una actualización de un ID inexistente no guarda una entidad.
4. Una actualización existente guarda la misma entidad modificada.
5. Eliminar un ID inexistente devuelve `false` y no llama a `delete`.
6. Eliminar una mascota existente llama a `delete`.
7. La búsqueda por especie utiliza el método derivado del repositorio.

## Pruebas del controlador

Se conservaron las rutas del controlador y se ajustaron las pruebas para usar `PetRepository` en H2. Antes de cada prueba se limpian los datos de esa base temporal. Las pruebas verifican el formulario, el rechazo de datos inválidos sin insertar, la creación válida, la búsqueda por especie, la lista vacía y los errores 404 para recursos inexistentes.

## Laboratorio de errores — Casos A a G

- **A. Variable ausente:** si falta `DB_PASSWORD`, Spring no puede resolver `${DB_PASSWORD}`. Es distinto de una contraseña incorrecta: en ese caso la variable existe, pero MariaDB rechaza la autenticación.
- **B. Puerto equivocado:** la conexión no se puede establecer porque la URL apunta a un puerto donde MariaDB no escucha. Es un error de conectividad, no de Thymeleaf.
- **C. Entidad sin `@Id`:** JPA necesita una clave primaria para identificar y administrar cada entidad; sin ella no puede mapearla correctamente.
- **D. Prueba vieja:** el import a `InMemoryPetRepository` falla al compilar porque la clase fue eliminada. Que MariaDB esté encendida no corrige un símbolo Java inexistente.
- **E. Dato inválido:** el formulario debe mostrar errores y el controlador no debe llamar al servicio ni al repositorio para crear el registro. Se puede comprobar que el total no aumentó con `SELECT COUNT(*) FROM pets;` antes y después.
- **F. Registro inexistente:** `/pets/99999999` debe devolver 404 porque el servicio recibe un `Optional` vacío y el controlador lo transforma en `ResponseStatusException(HttpStatus.NOT_FOUND)`. No se debe inventar una entidad vacía.
- **G. Base distinta:** el cliente SQL y `DB_URL` pueden apuntar a bases diferentes aunque sus nombres sean parecidos. Comprobar `SELECT DATABASE()` en el cliente y revisar la URL usada por Spring Boot.

## Reto integrador — Persistencia comprobable

La secuencia que se debe realizar en el equipo local es:

1. Registrar dos mascotas diferentes desde el formulario.
2. Anotar los identificadores reales que aparecen en las direcciones de detalle.
3. Abrir cada detalle y comprobar los datos.
4. Editar la edad de una de las mascotas.
5. Ejecutar un `SELECT` por su ID y comprobar que cambió esa fila y no la otra.
6. Eliminar la segunda mascota desde la interfaz.
7. Visitar la ruta de detalle de la mascota eliminada y comprobar el HTTP 404.
8. Detener Spring Boot con `Ctrl+C`, sin detener MariaDB.
9. Consultar `pets` desde el cliente MariaDB.
10. Iniciar otra vez Spring Boot y comprobar que la primera mascota conserva ID y datos modificados.
11. Ejecutar ` .\mvnw.cmd test ` en PowerShell y distinguir pruebas Mockito, JPA/H2 y MVC/H2.

Los resultados reales de esta secuencia dependen de ejecutar el proyecto con una instancia MariaDB configurada. No se incluyen capturas ni se afirma que esta comprobación manual se haya realizado desde este entorno.

## Ampliación opcional — Búsqueda por especie

Se implementó el método derivado y una prueba que guarda una mascota `Perro` y otra `Gato`, consulta `PERRO` y comprueba que solo devuelve las mascotas de esa especie en orden alfabético. La ruta web existente sigue recibiendo el parámetro `species`; no se creó un controlador nuevo.

## Diagnóstico final — Recorrido completo

### Situación A: registrar

- El navegador envía `POST /pets`.
- Spring MVC enlaza los campos con un objeto `PetForm` mediante `@ModelAttribute`.
- `@Valid` ejecuta las restricciones de Bean Validation y `BindingResult` permite revisar errores.
- Si hay errores, `PetController` devuelve la vista del formulario y no llama al servicio.
- Si no hay errores, `PetService` vuelve a verificar las reglas y usa `strip()` para quitar espacios externos.
- `PetService` construye `new Pet(...)` sin identificador y llama a `petRepository.save(pet)`.
- Spring Data JPA delega en JPA/Hibernate; MariaDB genera el ID con la columna de identidad.
- El controlador recibe el objeto guardado y redirige a `/pets/{id}` sin escribir SQL porque esa tarea corresponde a JPA/Hibernate.

### Situación B: consultar `/pets/7`

- La ruta la recibe `PetController` mediante `@GetMapping("/{id}")`.
- `@PathVariable` obtiene el ID desde la URL.
- `PetService.findById(id)` llama a `PetRepository.findById(id)`.
- `Optional<Pet>` representa que la mascota puede existir o no.
- Si existe, la entidad se añade al `Model` y Thymeleaf lee sus getters para presentar la vista.
- Si no existe, el controlador lanza una respuesta HTTP 404.

### Situación C: editar

- No se crea otra mascota con un ID arbitrario porque se debe conservar la identidad del registro.
- `updateDetails(...)` modifica nombre, especie, edad y descripción, pero no cambia el ID.
- El servicio llama a `save(...)` para persistir los cambios.
- Se verifica consultando la fila por su ID con SQL y comprobando los nuevos valores.

### Situación D: reiniciar

- Al detener Java dejan de existir los objetos y el contexto de Spring que estaban en memoria.
- Los registros guardados correctamente permanecen en MariaDB.
- Al consultar de nuevo, `findAll()` recupera las entidades mediante el repositorio y entrega una `List<Pet>` al controlador y a Thymeleaf.
- Los identificadores no comienzan necesariamente en 1 porque los genera la base de datos y pueden existir saltos.

## Conceptos para la comprobación final

Persistencia es conservar datos después de terminar la aplicación. Tabla es el conjunto estructurado de registros; fila es un registro; columna es una propiedad del registro; clave primaria identifica cada fila; SQL permite consultar y modificar datos; JDBC comunica Java con bases relacionales; driver es la biblioteca específica del motor; DataSource administra conexiones; ORM relaciona objetos y tablas; JPA define la especificación; Hibernate implementa el mapeo; entidad es una clase persistente; repositorio accede a datos; `JpaRepository` ofrece operaciones CRUD; ID generado es un identificador asignado por la base; y contexto de persistencia es el mecanismo que sigue las entidades durante una unidad de trabajo.

---

# Resumen de archivos modificados

| Archivo | Cambio |
|---|---|
| `pom.xml` | Se agregaron Spring Data JPA, MariaDB Connector/J, soporte de pruebas JPA, H2 y Mockito JUnit Jupiter. |
| `src/main/resources/application.yaml` | Conexión mediante variables de entorno y Hibernate `ddl-auto: update`. |
| `model/Pet.java` | Entidad JPA, conservando la API utilizada por el resto del proyecto. |
| `repository/PetRepository.java` | Nueva interfaz `JpaRepository<Pet, Long>` y método derivado de búsqueda por especie. |
| `repository/InMemoryPetRepository.java` | Eliminado porque ya no se utiliza almacenamiento en un `Map`. |
| `service/PetService.java` | Adaptado al repositorio JPA, conservando validación y normalización. |
| `src/test/resources/application.yaml` | Configuración H2 exclusiva para pruebas. |
| `repository/PetRepositoryTests.java` | Nuevas pruebas JPA. |
| `repository/InMemoryPetRepositoryTests.java` | Eliminada porque probaba una implementación retirada. |
| `service/PetServiceTests.java` | Reemplazada por pruebas unitarias con Mockito. |
| `controller/PetControllerIntegrationTests.java` | Adaptada para ejecutar con H2. |

## Nota de ejecución

Desde PowerShell, en la carpeta del proyecto y después de preparar MariaDB y las variables de entorno:

```powershell
.\mvnw.cmd spring-boot:run
```

Para ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```

La aplicación normal utiliza MariaDB. Las pruebas utilizan H2 o mocks según su tipo. No se debe utilizar `-DskipTests` para ocultar fallos, porque omitir las pruebas no demuestra que el código esté correcto. No se debe cambiar `ddl-auto` a `create` o `create-drop` en la configuración principal para solucionar errores, porque podría afectar los datos persistentes.
''', encoding='utf-8')
