# Laboratorio 12 — Diseño e implementación de una API REST con Spring Boot, JSON y seguridad

## Identificación del proyecto

- **Proyecto:** PetMatch Mini
- **Laboratorio:** 12
- **Tecnologías:** Java 21, Spring Boot 4.1.2, Spring MVC, Spring Security, Jakarta Validation, Jackson, Spring Data JPA, Hibernate, MariaDB y H2 para pruebas.

- **Propósito:** agregar una API REST que recibe y entrega JSON, reutilizando los servicios y repositorios de la aplicación web creada en los laboratorios anteriores.



## 1. Preparación y diagnóstico inicial

Se conserva la aplicación web existente:

- `PetController` continúa atendiendo las páginas HTML de mascotas.
- `SupportRequestController` continúa atendiendo las páginas HTML de solicitudes.
- `PetService` y `SupportRequestService` conservan las reglas del dominio.
- `PetRepository` y `SupportRequestRepository` continúan accediendo a MariaDB.
- `AppUser`, `UserRole` y `DatabaseUserDetailsService` conservan el registro y la autenticación existentes.
- Las plantillas Thymeleaf siguen siendo la interfaz web y H2 continúa siendo la base aislada de pruebas.

La API se agregó bajo `/api/v1/`. No se sustituyeron las rutas HTML `/pets` ni `/pets/{petId}/requests`.

### Dependencias revisadas

El `pom.xml` del Laboratorio 11 ya contiene las dependencias solicitadas por el PDF: Spring MVC, Validation, Security, sus dependencias de pruebas, Thymeleaf, JPA, MariaDB y H2. Por eso se conservaron sin duplicarlas ni agregar un iniciador REST innecesario.

### Diagnóstico inicial: respuestas

1. **¿Qué hace `PetService.create(...)` que no debería repetirse en un controlador REST?** Valida y normaliza los datos, construye la entidad `Pet` y la guarda utilizando `PetRepository`. El controlador solo debe recibir la petición HTTP, convertirla y llamar al servicio.
2. **¿Por qué `Pet` es una entidad JPA y `PetForm` no lo es?** `Pet` representa un registro persistente de la base de datos y está mapeada con `@Entity`. `PetForm` es un objeto para recibir y validar datos del formulario o del servicio; no representa una tabla.
3. **¿Qué diferencia hay entre `USER` y `ADMIN`?** Ambos pueden consultar recursos y crear solicitudes de apoyo mediante la API. Las operaciones administrativas, como crear, actualizar o eliminar mascotas y cambiar estados de solicitudes, requieren `ADMIN`.
4. **¿Por qué una solicitud `CANCELLED` no puede completarse aunque el usuario sea administrador?** El rol permite entrar a la ruta, pero `SupportRequestService` y `SupportRequest` verifican que la solicitud esté `OPEN`. La autorización no elimina las reglas de negocio.
5. **¿Por qué `passwordHash` nunca debe aparecer en una respuesta HTTP?** Es información interna de autenticación. Aunque sea un hash y no la contraseña legible, exponerlo aumenta el riesgo de seguridad y el cliente no lo necesita.
6. **¿Qué diferencia hay entre `/pets` y `/api/v1/pets`?** `/pets` usa el controlador MVC y Thymeleaf para responder HTML. `/api/v1/pets` usa un controlador REST para responder JSON. Ambas rutas reutilizan `PetService` y la misma base de datos.



## 2. Práctica 12.1 — Comprender HTTP, REST y JSON

### Conceptos

- **HTTP:** protocolo utilizado para que un cliente envíe peticiones y un servidor responda.
- **API:** interfaz que permite que otro programa se comunique con la aplicación mediante operaciones y datos definidos.
- **REST:** estilo de diseño de una API que organiza la comunicación alrededor de recursos y utiliza métodos HTTP para expresar las operaciones.
- **Recurso:** elemento del sistema que se identifica mediante una URI, por ejemplo, una mascota o una solicitud de apoyo.
- **Representación:** formato en el que se entrega un recurso. PetMatch puede representar una mascota como HTML en la web o como JSON en la API.
- **JSON:** formato textual de intercambio de datos que utiliza objetos, arreglos, cadenas, números, booleanos y `null`.
- **URI:** dirección que identifica un recurso dentro de la API. Las colecciones se expresan con nombres plurales, por ejemplo `/api/v1/pets`.
- **Método HTTP:** indica la intención de la operación. `GET` consulta, `POST` crea o ejecuta una acción, `PUT` actualiza o reemplaza y `DELETE` solicita eliminar.
- **Cabecera `Accept`:** indica el formato de respuesta que acepta el cliente.
- **Cabecera `Content-Type`:** indica el formato del cuerpo que envía el cliente.
- **Cabecera `Authorization`:** contiene las credenciales o el mecanismo de autenticación utilizado.
- **Código HTTP:** informa el resultado de la petición, por ejemplo `200`, `201`, `204`, `400`, `401`, `403`, `404` o `409`.
- **Idempotencia:** propiedad por la que repetir una operación produce el mismo efecto final deseado. `PUT` y `DELETE` suelen ser idempotentes; `POST` no lo es de forma general.

### Rutas de la API

| Método | Ruta | Uso | Respuesta esperada si es correcto |
|---|---|---|---|
| GET | `/api/v1/pets` | Consultar mascotas | 200 + arreglo JSON |
| GET | `/api/v1/pets/{petId}` | Consultar una mascota | 200 + JSON |
| POST | `/api/v1/pets` | Crear mascota (ADMIN) | 201 + `Location` + JSON |
| PUT | `/api/v1/pets/{petId}` | Actualizar mascota (ADMIN) | 200 + JSON |
| DELETE | `/api/v1/pets/{petId}` | Eliminar mascota (ADMIN) | 204 sin cuerpo |
| GET | `/api/v1/pets/{petId}/requests` | Consultar solicitudes | 200 + arreglo JSON |
| GET | `/api/v1/pets/{petId}/requests/{requestId}` | Consultar una solicitud | 200 + JSON |
| POST | `/api/v1/pets/{petId}/requests` | Crear solicitud (USER o ADMIN) | 201 + `Location` + JSON |
| POST | `/api/v1/pets/{petId}/requests/{requestId}/complete` | Completar solicitud (ADMIN) | 204 sin cuerpo |
| POST | `/api/v1/pets/{petId}/requests/{requestId}/cancel` | Cancelar solicitud (ADMIN) | 204 sin cuerpo |
| PUT | `/api/v1/pets/{petId}/requests/{requestId}/replace` | Reemplazar solicitud (ADMIN) | 201 + `Location` + JSON |

Las acciones de completar y cancelar usan `POST` porque son operaciones explícitas del dominio. El cliente no envía un estado libre como `COMPLETED`; el servicio ejecuta `complete()` o `cancel()`.

### Códigos HTTP principales

| Código | Significado en PetMatch Mini |
|---|---|
| 200 OK | Consulta o actualización correcta con contenido JSON. |
| 201 Created | Se creó un recurso. La respuesta informa la ubicación mediante `Location`. |
| 204 No Content | Operación correcta sin cuerpo de respuesta. |
| 400 Bad Request | JSON ilegible, parámetro incorrecto o datos que no cumplen la validación. |
| 401 Unauthorized | No se presentaron credenciales válidas para la API. |
| 403 Forbidden | El usuario autenticado no tiene el rol necesario o una protección como CSRF rechaza la petición. |
| 404 Not Found | El recurso no existe o la solicitud no pertenece a la mascota indicada. |
| 409 Conflict | Una regla de negocio impide ejecutar la operación con el estado actual. |

### Ejercicio: predice la respuesta

1. **Persona autenticada consulta una mascota existente:** `200 OK`, con el DTO de respuesta en JSON.
2. **Crear una mascota sin nombre:** `400 Bad Request`, con el código `validation_failed` y el error del campo `name`.
3. **Una cuenta USER intenta eliminar una mascota:** `403 Forbidden`, porque la ruta está reservada a ADMIN.
4. **ADMIN intenta completar una solicitud CANCELLED:** `409 Conflict`, porque el estado actual no permite completar la solicitud.
5. **Cliente sin credenciales consulta la lista:** `401 Unauthorized`.
6. **Eliminar una mascota sin solicitudes relacionadas con ADMIN:** `204 No Content`, sin cuerpo.
7. **Consultar una solicitud utilizando el identificador de otra mascota:** `404 Not Found`, porque el servicio comprueba la relación entre `petId` y `requestId`.

## 3. Práctica 12.2 — Separar entidades JPA, contratos JSON y mapeadores

### Conceptos

- **DTO (Data Transfer Object):** objeto que define los datos que entran o salen de una operación. Permite controlar qué propiedades acepta o expone la API.
- **DTO de entrada:** representa los datos que el cliente puede enviar. No debe incluir identificadores o estados que controla el servidor.
- **DTO de salida:** define la información que se devuelve al cliente, sin exponer directamente las entidades JPA ni datos internos.
- **`record`:** tipo de Java que permite declarar datos de forma concisa e inmutable, con constructor y métodos de acceso.
- **Mapper:** componente que convierte un DTO REST a un formulario que entiende el servicio, o una entidad a un DTO de respuesta. No consulta repositorios ni ejecuta reglas de negocio.
- **Jakarta Validation:** conjunto de anotaciones que permite validar entradas como `@NotBlank`, `@NotNull`, `@Size` y `@Min`.
- **Serialización JSON:** proceso que convierte un objeto Java en una representación JSON para enviarla al cliente.
- **Deserialización JSON:** proceso inverso: interpreta el cuerpo JSON y construye el objeto Java declarado por el controlador.

### DTO creados

| Archivo | Responsabilidad |
|---|---|
| `api/dto/PetRequest.java` | Entrada permitida para crear o actualizar mascotas. |
| `api/dto/PetResponse.java` | Datos públicos de una mascota guardada. |
| `api/dto/SupportRequestRequest.java` | Entrada de una solicitud: tipo y descripción. |
| `api/dto/SupportRequestResponse.java` | Salida de una solicitud: id, petId, tipo, descripción y estado. |
| `api/dto/ApiErrorResponse.java` | Contrato de error: código, mensaje y errores por campo. |
| `api/mapper/PetApiMapper.java` | Convierte `PetRequest` a `PetForm` y `Pet` a `PetResponse`. |
| `api/mapper/SupportRequestApiMapper.java` | Convierte `SupportRequestRequest` a `SupportRequestForm` y una entidad a `SupportRequestResponse`. |

`PetRequest` valida nombre y especie obligatorios, edad no nula y no negativa, y longitudes máximas. `SupportRequestRequest` exige un tipo y una descripción no vacía de máximo 500 caracteres. Los identificadores y el estado no forman parte de los DTO de entrada.

`SupportType` solo admite `WALK`, `FEEDING` y `COMPANY`. El valor `VETERINARY` no existe en el enum actual y no puede convertirse mediante JSON.

### ¿Por qué no devolver entidades JPA?

Devolver `Pet`, `SupportRequest` o `AppUser` directamente acoplaría el contrato HTTP a las tablas y relaciones internas. Además, puede exponer campos no necesarios, provocar ciclos al serializar relaciones o acceder a asociaciones `LAZY`. Por eso los controladores devuelven DTOs. En particular, `passwordHash` no aparece en ninguna respuesta de esta API.

### Ejercicio: diseñar un contrato de registro

Para un DTO de entrada de registro se necesitan `displayName`, `email`, `password` y `confirmPassword`, porque son los datos necesarios para crear la cuenta y comprobar la contraseña. Una respuesta pública puede mostrar `id`, `displayName` y `email` cuando esos datos se necesiten. `password`, `confirmPassword` y `passwordHash` nunca deben devolverse. `role` y `enabled` son valores administrados por el servidor; no deben permitirse como campos de entrada del registro público. Solo podrían exponerse en una respuesta privada o administrativa si existiera una necesidad y autorización explícitas.

### Depuración: errores de representación

- Si el cliente envía `"age": "three"`, el conversor no puede transformar el texto en `Integer`; se responde 400 con `invalid_json`.
- Si envía `"age": -1`, el JSON sí se convierte, pero no cumple `@Min(0)`; se responde 400 con `validation_failed` y el campo `age`.
- Si el controlador devuelve directamente una entidad con una relación `LAZY`, puede provocar consultas adicionales o un error de inicialización. Se deben devolver DTOs.
- Si `passwordHash` aparece en una respuesta, el contrato de salida está exponiendo información interna. Se debe retirar del DTO y comprobarlo con pruebas.

## 4. Práctica 12.3 — Construir controladores REST sin duplicar el negocio

### Conceptos

- **`@Controller`:** controlador MVC tradicional que normalmente devuelve el nombre de una plantilla.
- **`@RestController`:** controlador cuyo valor de retorno se escribe en el cuerpo HTTP y se convierte a JSON cuando corresponde.
- **`@RequestBody`:** indica que el parámetro Java se construye a partir del cuerpo de la petición.
- **`@Valid`:** activa las restricciones de validación declaradas en el DTO recibido.
- **`@PathVariable`:** obtiene un valor de la ruta, como `petId` o `requestId`.
- **`ResponseEntity`:** permite controlar el código HTTP, las cabeceras y el cuerpo de una respuesta.
- **Cabecera `Location`:** identifica la URL del recurso que acaba de crearse.

### Archivos creados

| Archivo | Función |
|---|---|
| `api/controller/PetRestController.java` | Endpoints REST para consultar, crear, actualizar y eliminar mascotas. |
| `api/controller/SupportRequestRestController.java` | Endpoints REST para consultar, crear, completar, cancelar y reemplazar solicitudes. |

Los controladores no contienen consultas SQL, no crean repositorios paralelos y no repiten las reglas de `PetService` ni `SupportRequestService`.

### Recorrido de una creación de mascota

1. El cliente envía JSON a `POST /api/v1/pets`.
2. Spring convierte el JSON en `PetRequest`.
3. `@Valid` ejecuta las restricciones de Jakarta Validation.
4. `PetApiMapper.toForm(...)` convierte el DTO en `PetForm`.
5. `PetService.create(...)` normaliza y valida los datos.
6. `PetRepository` guarda la entidad y la base genera el identificador.
7. `PetApiMapper.toResponse(...)` construye `PetResponse`.
8. El controlador responde `201 Created`, agrega `Location` y entrega el JSON.

### Recurso anidado y estado de solicitudes

La ruta `/api/v1/pets/{petId}/requests/{requestId}` identifica una solicitud dentro de una mascota. El servicio utiliza `findByIdAndPet_Id(requestId, petId)` para comprobar la pertenencia; no basta con que exista el identificador de solicitud.

Para cambiar estados se utilizan `requestService.complete(...)` y `requestService.cancel(...)`. El controlador no modifica directamente `status`. El método `replace(...)` ya existente cancela la solicitud abierta y crea una nueva dentro de la transacción definida en el servicio. Por eso la respuesta de reemplazo es `201 Created` y la ubicación apunta a la nueva solicitud.

### Ejercicio: recorrido de POST `/api/v1/pets`

El recorrido completo es el que aparece en los ocho pasos anteriores: deserialización del JSON, validación, conversión a `PetForm`, normalización del servicio, persistencia, generación del identificador, mapeo a `PetResponse` y respuesta `201 Created` con `Location`.

## 5. Práctica 12.4 — Separar la seguridad de la API y la web

### Conceptos

- **`SecurityFilterChain`:** conjunto de filtros y reglas de seguridad que se aplica a las peticiones que coinciden con su alcance.
- **`securityMatcher`:** define qué rutas seleccionan una cadena de filtros.
- **`@Order`:** establece la prioridad de las cadenas; Spring utiliza la primera cadena cuyo matcher coincide.
- **HTTP Basic:** mecanismo que envía usuario y contraseña en la cabecera `Authorization`. Base64 es codificación, no cifrado; para redes no confiables debe usarse HTTPS.
- **`STATELESS`:** la cadena de la API no conserva la autenticación en una sesión HTTP; el cliente envía HTTP Basic en cada petición protegida. No significa que las operaciones no cambien la base de datos.
- **CSRF:** protección frente a peticiones forzadas que aprovechan la sesión del navegador. En este ejercicio se desactiva solo en la cadena `/api/**`; los formularios web mantienen CSRF.
- **JWT:** formato de token firmado que no se requiere en este laboratorio. No se incorpora JWT ni OAuth2.

### Configuración aplicada

Se reemplazó el contenido de `security/SecurityConfig.java` con dos cadenas:

1. `apiSecurityFilterChain`, con `@Order(1)` y `securityMatcher("/api/**")`. Utiliza HTTP Basic, sesiones `STATELESS` y CSRF desactivado únicamente para esa cadena.
2. `webSecurityFilterChain`, con `@Order(2)`. Conserva el login por formulario, logout, sesión HTTP, las reglas de roles y CSRF de la aplicación anterior.

### Resumen de permisos de la API

| Petición | USER | ADMIN | Sin credenciales |
|---|---:|---:|---:|
| GET `/api/v1/pets` | 200 | 200 | 401 |
| POST `/api/v1/pets` | 403 | Puede ejecutar el controlador | 401 |
| POST `/api/v1/pets/{id}/requests` | Puede ejecutar el controlador | Puede ejecutar el controlador | 401 |
| POST completar/cancelar solicitud | 403 | Puede ejecutar el servicio | 401 |
| PUT reemplazar solicitud | 403 | Puede ejecutar el servicio | 401 |

"Puede ejecutar" significa que superó la seguridad de ruta; todavía puede recibir 400, 404 o 409 según los datos y las reglas del dominio.

La API no incorpora propiedad de datos por usuario: el modelo no relaciona `Pet` o `SupportRequest` con `AppUser`. 

### Ejercicio: reconocer el punto de rechazo

- Una cuenta `USER` intenta borrar una mascota con solicitudes: recibe `403 Forbidden` en Spring Security, antes de ejecutar `PetService.delete(...)`.
- Una cuenta `ADMIN` intenta la misma operación: supera la autorización, llega al servicio y este detecta el historial; la respuesta es `409 Conflict`.

La autorización y las reglas de negocio son controles diferentes y ambos se conservan.

## 6. Práctica 12.5 — Errores JSON y comprobación con clientes HTTP

### Conceptos

- **`@RestControllerAdvice`:** componente que maneja excepciones de controladores REST y devuelve contenido en el cuerpo HTTP.
- **`ApiErrorResponse`:** DTO de error con `code`, `message` y `fieldErrors`.
- **`MethodArgumentNotValidException`:** se produce cuando un DTO convertido correctamente incumple una restricción de validación.
- **`HttpMessageNotReadableException`:** indica que el cuerpo no pudo leerse o convertirse, por ejemplo, por JSON mal formado o un valor de enum inexistente.
- **Conflicto de negocio:** ocurre cuando el recurso existe pero su estado impide la operación; se representa con 409.

### Manejador de errores REST

Se creó `api/error/RestApiExceptionHandler.java`. Está limitado a `PetRestController` y `SupportRequestRestController`, con prioridad alta para evitar que las excepciones de la API terminen en la página HTML del `PageErrorHandler`.

| Excepción o situación | Código HTTP | Código de error JSON |
|---|---:|---|
| DTO con campos inválidos | 400 | `validation_failed` |
| JSON ilegible o enum desconocido | 400 | `invalid_json` |
| Parámetro de ruta incompatible | 400 | `invalid_parameter` |
| Argumento inválido | 400 | `invalid_request` |
| Recurso inexistente o solicitud de otra mascota | 404 | `not_found` |
| Regla de negocio incumplida | 409 | `business_conflict` |
| Credenciales ausentes/incorrectas | 401 | Lo gestiona Spring Security antes del controlador; no se promete el DTO de error REST. |
| Rol insuficiente | 403 | Lo gestiona Spring Security antes del controlador; no se promete el DTO de error REST. |

No se convierten todas las excepciones en `400` ni se ocultan fallos inesperados con `200`. Los detalles internos del conversor o de la base de datos no se envían indiscriminadamente al cliente.

### Pruebas manuales con curl

Usa cuentas de prueba propias y un identificador real obtenido de la respuesta. Una cuenta recién registrada tiene rol `USER`; para crear/actualizar/eliminar mascotas o cambiar estados se necesita `ADMIN`. No guardes contraseñas reales en documentos, capturas o repositorios. En Windows se recomienda ejecutar `curl.exe` desde PowerShell.

**Consultar mascotas:**

```powershell
curl.exe -i --user "user@example.com" -H "Accept: application/json" http://localhost:8080/api/v1/pets
```

**Consultar una mascota concreta como USER:**

```powershell
curl.exe -i --user "user@example.com" -H "Accept: application/json" http://localhost:8080/api/v1/pets/ID_REAL
```

Usa un identificador que exista. La respuesta esperada es 200 y contiene los campos del `PetResponse`. Para un identificador inexistente se debe recibir 404 con `not_found`.

**Crear una mascota como ADMIN:**

```powershell
curl.exe -i --user "admin@example.com" -H "Content-Type: application/json" -X POST -d '{"name":"Kira","species":"Dog","age":4}' http://localhost:8080/api/v1/pets
```

La respuesta correcta es `201`, contiene el JSON y una cabecera `Location`. Copia el id real de la URL devuelta.

**Actualizar una mascota como ADMIN:**

```powershell
curl.exe -i --user "admin@example.com" -H "Content-Type: application/json" -X PUT -d '{"name":"Kira","species":"Dog","age":5,"description":"Enjoys long walks."}' http://localhost:8080/api/v1/pets/ID_REAL
```

La respuesta esperada es 200 con el `PetResponse` actualizado. Confirma el cambio con otro GET y desde la página HTML.

**Crear solicitud de apoyo como USER:**

```powershell
curl.exe -i --user "user@example.com" -H "Content-Type: application/json" -X POST -d '{"type":"WALK","description":"Needs a short walk."}' http://localhost:8080/api/v1/pets/ID_REAL/requests
```

La respuesta correcta es `201`, el estado inicial es `OPEN` y existe una cabecera `Location`. Si ya hay una solicitud abierta para esa mascota, el servicio responde `409`.

**Consultar las solicitudes de una mascota:**

```powershell
curl.exe -i --user "user@example.com" -H "Accept: application/json" http://localhost:8080/api/v1/pets/ID_REAL/requests
```

El resultado esperado es 200 y un arreglo JSON. Para consultar una solicitud concreta usa la URL que termina en `/requests/ID_SOLICITUD`.

```powershell
curl.exe -i --user "user@example.com" -H "Accept: application/json" http://localhost:8080/api/v1/pets/ID_REAL/requests/ID_SOLICITUD
```

**Completar una solicitud como ADMIN:**

```powershell
curl.exe -i --user "admin@example.com" -X POST http://localhost:8080/api/v1/pets/ID_REAL/requests/ID_SOLICITUD/complete
```

La respuesta correcta es `204` sin cuerpo. Comprueba el estado con GET o desde la interfaz HTML.

**Cancelar una solicitud como ADMIN:**

```powershell
curl.exe -i --user "admin@example.com" -X POST http://localhost:8080/api/v1/pets/ID_REAL/requests/ID_SOLICITUD/cancel
```

La respuesta esperada es 204. La solicitud queda en estado `CANCELLED` y no puede completarse después.

**Reemplazar una solicitud abierta como ADMIN:**

```powershell
curl.exe -i --user "admin@example.com" -H "Content-Type: application/json" -X PUT -d '{"type":"COMPANY","description":"Needs company."}' http://localhost:8080/api/v1/pets/ID_REAL/requests/ID_SOLICITUD/replace
```

La respuesta esperada es 201 con `Location` para la solicitud nueva. La anterior queda `CANCELLED` y la nueva queda `OPEN`.

**Probar un tipo de solicitud no existente:**

```powershell
curl.exe -i --user "user@example.com" -H "Content-Type: application/json" -X POST -d '{"type":"VETERINARY","description":"Needs assistance."}' http://localhost:8080/api/v1/pets/ID_REAL/requests
```

La respuesta esperada es 400 con `invalid_json`, porque `VETERINARY` no pertenece al enum `SupportType`.

**Probar una edad negativa:**

```powershell
curl.exe -i --user "admin@example.com" -H "Content-Type: application/json" -X POST -d '{"name":"Kira","species":"Dog","age":-4}' http://localhost:8080/api/v1/pets
```

La respuesta esperada es `400` con `code` igual a `validation_failed` y error para `age`.

**Consultar sin credenciales:**

```powershell
curl.exe -i -H "Accept: application/json" http://localhost:8080/api/v1/pets
```

La respuesta esperada es `401 Unauthorized`; HTTP Basic suele agregar `WWW-Authenticate`.

**Intentar crear mascota como USER:**

```powershell
curl.exe -i --user "user@example.com" -H "Content-Type: application/json" -X POST -d '{"name":"Kira","species":"Dog","age":4}' http://localhost:8080/api/v1/pets
```

La respuesta esperada es `403`, sin insertar la mascota.

### Inspección de MariaDB

Después de crear registros desde la API, se pueden comprobar con consultas de solo lectura:

```sql
SELECT id, name, species, age, description
FROM pets
ORDER BY id DESC;

SELECT id, pet_id, type, description, status
FROM support_requests
ORDER BY id DESC;
```

Los cambios también deben observarse desde la interfaz web, porque ambas entradas reutilizan los mismos servicios y repositorios. No se necesita crear páginas Thymeleaf para los recursos JSON.

### Ejercicio: tabla de verificación

Para cada llamada realizada con el cliente HTTP registra método y ruta, rol utilizado, código esperado, código obtenido, cabeceras relevantes (`Content-Type`, `Location`, `WWW-Authenticate` cuando aplique), estructura del cuerpo JSON y efecto observable en MariaDB. Si algo no coincide, identifica primero si el problema fue de autenticación, autorización, validación, búsqueda, regla de negocio o persistencia.

| Método y ruta (ejemplo) | Rol | Código esperado | Qué revisar |
|---|---|---:|---|
| GET `/api/v1/pets` | USER | 200 | `Content-Type` y arreglo JSON. |
| GET `/api/v1/pets` | Sin credenciales | 401 | `WWW-Authenticate` si se envía. |
| POST `/api/v1/pets` | USER | 403 | No debe insertarse mascota. |
| POST `/api/v1/pets` | ADMIN | 201 | `Location`, cuerpo e inserción. |
| POST solicitud con edad de mascota válida | USER | 201 | `status: OPEN` y `petId`. |
| POST solicitud duplicada abierta | USER | 409 | Código `business_conflict`, no crea otra. |
| POST completar solicitud abierta | ADMIN | 204 | Sin cuerpo y estado persistido. |
| POST completar solicitud cerrada | ADMIN | 409 | `business_conflict`. |

### Depuración

- **302 hacia `/login` al llamar la API:** revisar que la ruta empiece por `/api/` y que se seleccione la cadena API con prioridad 1.
- **401 con credenciales que parecen correctas:** revisar correo normalizado, contraseña, atributo `enabled` y `DatabaseUserDetailsService`.
- **403 con credenciales válidas:** verificar rol; en formularios web revisar también CSRF.
- **400 `invalid_json`:** revisar sintaxis JSON, `Content-Type` y valores enum.
- **400 `validation_failed`:** el JSON se convirtió, pero algún campo incumple una anotación de validación.
- **404 al consultar solicitud:** verificar que `petId` y `requestId` pertenecen a la misma relación.
- **409 al completar:** verificar que la solicitud no está `COMPLETED` o `CANCELLED`.
- **415:** enviar `Content-Type: application/json` en POST o PUT con cuerpo JSON.
- **500:** revisar logs y causa real; no convertirlo en un error de validación.

## 7. Práctica 12.6 — Pruebas automatizadas, depuración y reto integrador

### Conceptos

- **Prueba unitaria:** verifica una clase o transformación de forma aislada, sin iniciar el servidor.
- **Prueba de integración:** comprueba la interacción entre varias partes del sistema, por ejemplo, filtros de seguridad, controlador, servicio y repositorio.
- **MockMvc:** permite construir peticiones HTTP de prueba que pasan por Spring MVC y por los filtros de seguridad.
- **H2:** base de datos en memoria utilizada durante las pruebas para no depender de MariaDB de desarrollo.
- **`httpBasic(...)`:** procesador de pruebas que agrega cabecera HTTP Basic; en este laboratorio permite comprobar credenciales contra el `UserDetailsService` real.
- **`@WithMockUser` / `user(...)`:** crea una identidad simulada para una prueba de autorización; no comprueba la contraseña almacenada.

### Pruebas agregadas

| Archivo | Qué verifica |
|---|---|
| `api/mapper/PetApiMapperTests.java` | Conversión de `PetRequest` a `PetForm`, descripción opcional y conversión de entidad a `PetResponse`. |
| `api/controller/RestApiIntegrationTests.java` | HTTP Basic, roles, validación JSON, recursos ausentes, creación, solicitudes anidadas, cambios de estado, reemplazo transaccional, eliminación y separación CSRF web/API. |

Las pruebas de integración preparan una cuenta `USER` y una cuenta `ADMIN` temporal en H2. La cuenta ADMIN se crea inicialmente como USER y se promueve solo en la base temporal de pruebas mediante un `UPDATE` parametrizado. No se añade ningún endpoint de registro de administradores y el formulario público sigue asignando USER.

### Reto integrador: una API coherente para dos clientes

El recorrido solicitado se realiza así:

1. Inicia sesión como ADMIN en la web y registra una mascota de prueba.
2. Consulta esa mascota mediante `GET /api/v1/pets/{petId}` usando HTTP Basic.
3. Utiliza una cuenta USER para crear una solicitud mediante `POST /api/v1/pets/{petId}/requests` con JSON.
4. Abre la lista HTML de solicitudes de esa mascota y comprueba que aparece la misma solicitud.
5. Intenta completar la solicitud desde la API con USER: debe recibir `403`.
6. Completa la solicitud desde la API con ADMIN: debe recibir `204`.
7. Vuelve a la web y verifica que el estado aparece como `COMPLETED`.
8. Intenta completar de nuevo la solicitud: debe recibir `409`.
9. Intenta eliminar la mascota desde la API: debe recibir `409` por el historial relacionado.
10. Comprueba las filas con SQL y reinicia la aplicación para verificar la persistencia de MariaDB.

Las pruebas automatizadas de H2 verifican los contratos HTTP y parte de la persistencia de prueba. El paso de comprobación y reinicio de MariaDB debe ejecutarse manualmente con las variables de conexión del entorno local; no se afirma que se haya ejecutado en esta preparación.

### Diagnóstico final: explicación de POST `/api/v1/pets`

1. El cliente HTTP envía el JSON, `Authorization: Basic ...` y `Content-Type: application/json`.
2. Spring Security selecciona `apiSecurityFilterChain` para `/api/**`.
3. HTTP Basic autentica mediante `DatabaseUserDetailsService`.
4. La regla de seguridad exige el rol ADMIN para `POST /api/v1/pets`.
5. `PetRestController.create(...)` recibe el cuerpo como `PetRequest`.
6. `@Valid` aplica Jakarta Validation.
7. `PetApiMapper.toForm(...)` lo convierte a `PetForm`.
8. `PetService.create(...)` normaliza y valida los datos y persiste mediante `PetRepository`.
9. El mapper construye `PetResponse` y el controlador devuelve `201 Created`, `Location` y JSON.

**¿Qué cambia si la edad es negativa?** La validación rechaza la entrada antes de ejecutar el servicio y responde 400 con `validation_failed`.

**¿Qué cambia si la cuenta tiene rol USER?** La autenticación puede ser correcta, pero la autorización rechaza el POST con 403 antes del controlador.

**¿Qué cambia si no envía credenciales?** La API responde 401 antes de llegar al controlador.

**¿Qué cambia si una solicitud ya fue cancelada?** La ruta administrativa puede estar autorizada, pero el servicio y el dominio no permiten cambiar ese estado; el manejador REST responde 409.


## 8. Verificación final

La lista que debe comprobarse localmente es:

- La web `/pets` continúa respondiendo HTML con la sesión web.
- GET `/api/v1/pets` con HTTP Basic válido responde 200 y arreglo JSON.
- GET `/api/v1/pets` sin credenciales responde 401.
- GET de mascota inexistente responde 404 con `not_found`.
- POST de mascota con USER responde 403 sin insertar.
- POST de mascota válido con ADMIN responde 201 con `Location`.
- POST de mascota con edad negativa responde 400 con `validation_failed`.
- JSON mal formado responde 400 con `invalid_json`.
- POST de solicitud válido con USER responde 201 y estado OPEN.
- Segunda solicitud abierta para la misma mascota responde 409.
- Solicitud consultada bajo otra mascota responde 404.
- Completar con USER responde 403.
- Completar con ADMIN y estado OPEN responde 204 y persiste COMPLETED.
- Completar una solicitud cerrada responde 409.
- PUT de reemplazo responde 201; anterior CANCELLED y nueva OPEN.
- POST web sin CSRF responde 403.
- Las cuentas de registro público mantienen el rol USER.
- Los registros continúan en MariaDB después de reiniciar la aplicación.

## 9. Ejecución del proyecto

### Variables de entorno de MariaDB

En Windows PowerShell:

```powershell
$env:DB_URL="jdbc:mariadb://localhost:3306/petmatch_lab"
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
```

### Ejecutar las pruebas

```powershell
.\mvnw.cmd test
```

### Iniciar la aplicación

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación se abre en `http://localhost:8080`. La API queda disponible bajo `http://localhost:8080/api/v1/`.

