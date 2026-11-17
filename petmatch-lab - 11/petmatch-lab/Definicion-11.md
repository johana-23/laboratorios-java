# Laboratorio 11 — Registro, autenticación y autorización con Spring Security

## Identificación del proyecto

- **Proyecto:** PetMatch Mini
- **Laboratorio:** 11
- **Tecnologías:** Java 21, Spring Boot 4.1.2, Spring Security, Spring MVC, Thymeleaf, Spring Data JPA, MariaDB y H2 para pruebas.
- **Paquete Java conservado:** `com.example.petmatch_lab`.
- **Propósito:** incorporar cuentas de usuario, registro, inicio y cierre de sesión, roles `USER` y `ADMIN`, protección de rutas y CSRF.


### Diagnóstico inicial

1. **Estar registrado e iniciar sesión no es lo mismo.** Estar registrado significa que la cuenta fue creada y guardada. Iniciar sesión significa que la persona presentó sus credenciales y Spring Security comprobó que corresponden a esa cuenta.
2. **La contraseña no debe guardarse como texto legible.** Se almacena un hash generado con `PasswordEncoder`, para que no sea posible recuperar directamente la contraseña original.
3. **Una persona autenticada no puede eliminar automáticamente cualquier mascota.** La autenticación identifica al usuario; la autorización determina si su rol permite esa operación.
4. **Ocultar un botón no protege una ruta.** Una persona puede construir una petición HTTP manualmente sin utilizar la interfaz. La protección debe estar en el servidor, antes de llegar al controlador.
5. **Las reglas de `SupportRequestService` deben conservarse.** Spring Security controla quién puede invocar la operación, pero el servicio comprueba si la transición es válida para el estado actual de la solicitud.

### Rutas y métodos que se protegen

| Ruta | Método | Regla aplicada |
|---|---|---|
| `/`, `/about`, `/login`, `/register` | GET | Público |
| `/pets` y `/pets/{id}` | GET | Usuario autenticado |
| `/pets/new`, `/pets/{id}/edit` | GET | ADMIN |
| `/pets`, `/pets/{id}`, `/pets/{id}/delete` | POST | ADMIN |
| `/pets/{petId}/requests` | GET | Usuario autenticado |
| `/pets/{petId}/requests/new` | GET | Usuario autenticado |
| `/pets/{petId}/requests` | POST | USER o ADMIN |
| Completar, cancelar o reemplazar una solicitud | POST/GET de reemplazo | ADMIN |
| `/admin` | GET | ADMIN |
| `/profile` | GET | Usuario autenticado |

## 2. Práctica 11.1 — Identificación, autenticación y autorización

### Conceptos

- **Identificación:** dato con el que una persona indica qué cuenta utiliza. En PetMatch Mini se utiliza el correo electrónico.
- **Autenticación:** comprobación de que la persona conoce las credenciales de la cuenta, por ejemplo, correo y contraseña.
- **Autorización:** comprobación de las acciones que la cuenta puede realizar según sus permisos o rol.
- **Registro:** proceso de crear una cuenta nueva. En este laboratorio el registro no inicia sesión automáticamente; redirige a `/login?registered`.
- **Sesión HTTP:** mecanismo que permite mantener el estado de autenticación entre varias peticiones web.
- **Cookie de sesión:** dato que el navegador envía en las peticiones posteriores para identificar la sesión del servidor. No contiene la contraseña.
- **Rol:** agrupación de permisos. Se utilizan `USER` y `ADMIN`.
- **CSRF:** ataque en el que un sitio externo intenta provocar que el navegador de una persona autenticada envíe una petición no deseada.

### Diferencia entre 401, 403 y 302

- **401 Unauthorized:** indica que se requiere autenticación válida; es común en APIs.
- **403 Forbidden:** la petición está prohibida por los permisos actuales o fue rechazada por una protección como CSRF.
- **302 Found:** indica una redirección. En esta aplicación web, un visitante sin sesión que solicita una ruta protegida es enviado al formulario `/login`.

### Política de roles

| Operación | Visitante | USER | ADMIN |
|---|---|---|---|
| Abrir `/login` | Sí | Sí | Sí |
| Abrir `/register` | Sí | Sí | Sí |
| Abrir `/pets` | Redirección al login | Sí | Sí |
| Registrar mascota | No | No | Sí |
| Registrar solicitud de apoyo | No | Sí | Sí |
| Completar solicitud | No | No | Sí |
| Abrir `/admin` | No | No | Sí |

En Spring Security `hasRole("ADMIN")` comprueba la autoridad `ROLE_ADMIN`. La entidad almacena `ADMIN` como valor de `UserRole`; el adaptador de autenticación agrega el prefijo `ROLE_`.

### Análisis de amenazas

1. **Contraseña visible:** si se almacena directamente, quien obtenga una copia de la base podría leer las contraseñas.
2. **Elevación de privilegios:** permitir elegir `ADMIN` en el registro público permitiría que cualquier visitante se otorgara permisos administrativos.
3. **Acceso directo:** esconder un botón no impide que alguien envíe el POST manualmente; la ruta debe protegerse en el servidor.
4. **Petición forzada:** una página externa podría intentar aprovechar la cookie de sesión de una persona autenticada; el token CSRF ayuda a rechazar la petición.

El rol no representa propiedad de los datos. En este proyecto no existe una relación entre `Pet` o `SupportRequest` y `AppUser`; por eso no se afirma que cada usuario solo vea sus propios registros.

## 3. Práctica 11.2 — Crear cuentas y registrar usuarios de forma segura

### Dependencias

Se agregaron al `pom.xml`, sin quitar las anteriores:

- `spring-boot-starter-security`
- `spring-boot-starter-security-test` con alcance `test`.

Se mantienen Spring MVC, Thymeleaf, Validation, Data JPA, MariaDB, H2 y las dependencias de pruebas del Laboratorio 10.

### Archivos agregados

| Archivo | Función |
|---|---|
| `model/UserRole.java` | Define los roles `USER` y `ADMIN`. |
| `model/AppUser.java` | Entidad JPA de las cuentas, tabla `app_users`. |
| `repository/AppUserRepository.java` | Busca cuentas por correo y comprueba si ya existe uno. |
| `dto/RegistrationForm.java` | Recibe nombre, correo, contraseña y confirmación. |
| `exception/DuplicateEmailException.java` | Representa un correo que ya está registrado. |
| `service/RegistrationService.java` | Normaliza el correo, valida datos, codifica la contraseña y guarda la cuenta. |
| `controller/RegistrationController.java` | Muestra y procesa el formulario de registro. |

### Entidad `AppUser`

La entidad contiene `id`, `displayName`, `email`, `passwordHash`, `role` y `enabled`. El correo tiene restricción `unique = true` para evitar duplicados en la base de datos. El constructor público asigna `UserRole.USER` y `enabled = true`.

El formulario no incluye un campo para elegir el rol. De esta manera, el navegador no puede solicitar que la cuenta nueva sea administradora.

### Validaciones y codificación

- El nombre es obligatorio y tiene máximo 100 caracteres.
- El correo es obligatorio, debe tener formato de correo y tiene máximo 190 caracteres.
- La contraseña debe tener entre 12 y 64 caracteres.
- La confirmación debe coincidir con la contraseña.
- El correo se elimina de espacios externos y se convierte a minúsculas con `Locale.ROOT`.
- La contraseña se transforma con `PasswordEncoder.encode(...)` antes de persistirse.

`PasswordEncoder.encode(rawPassword)` genera el valor que se almacena. `PasswordEncoder.matches(rawPassword, encodedPassword)` compara un intento con el valor almacenado. No se descifra el hash. Se utiliza `PasswordEncoderFactories.createDelegatingPasswordEncoder()`, que identifica el algoritmo con un prefijo como `{bcrypt}`.

No se utiliza texto plano, Base64, SHA-256 sin un diseño específico para contraseñas ni un algoritmo propio.

### Recorrido de registro

1. La persona abre `/register`.
2. Completa nombre, correo y contraseña dos veces.
3. Spring MVC ejecuta Bean Validation y el controlador comprueba que las contraseñas coincidan.
4. `RegistrationService` normaliza el correo y comprueba si ya existe.
5. El servicio codifica la contraseña y crea `AppUser` con rol `USER`.
6. `AppUserRepository` persiste la cuenta en `app_users`.
7. El navegador es redirigido a `/login?registered`.

La consulta `existsByEmail` permite presentar un mensaje claro antes de guardar. La restricción UNIQUE de MariaDB es necesaria como defensa final, porque dos solicitudes concurrentes podrían superar la consulta inicial.

### Comprobación en MariaDB

En la base de desarrollo, después de registrar una cuenta desde la interfaz:

```sql
SELECT id, display_name, email, role, enabled
FROM app_users
ORDER BY id;

SELECT email,
       LEFT(password_hash, 8) AS hash_prefix,
       CHAR_LENGTH(password_hash) AS hash_length
FROM app_users;
```

Se espera ver el rol `USER`, la cuenta habilitada y un prefijo de hash como `{bcrypt}`. No se debe imprimir ni compartir el hash completo ni la contraseña.

### Ejercicio de la práctica 11.2

**¿Por qué el rol se asigna dentro del constructor?** Para que el registro público solo cree cuentas `USER` y no permita elevar privilegios desde el formulario.

**¿Por qué se necesita `existsByEmail` y también UNIQUE?** La consulta permite detectar el duplicado y mostrar un mensaje antes de insertar. La restricción de base de datos protege también frente a dos registros simultáneos.

## 4. Práctica 11.3 — Autenticación con Spring Security y sesiones

### Adaptador `DatabaseUserDetailsService`

Implementa el contrato `UserDetailsService`. Su método `loadUserByUsername` recibe el correo, lo normaliza, consulta `AppUserRepository` y devuelve un `UserDetails` de Spring Security con:

- correo como nombre de usuario;
- `passwordHash` como contraseña codificada;
- autoridad `ROLE_USER` o `ROLE_ADMIN`;
- estado habilitado derivado de `AppUser.enabled`.

`AppUser` no implementa `UserDetails`: la entidad JPA y el objeto utilizado por Spring Security conservan responsabilidades separadas.

### Configuración `SecurityConfig`

- Declara el `PasswordEncoder` delegado.
- Declara las rutas públicas.
- Protege `/admin` y las operaciones administrativas con `hasRole("ADMIN")`.
- Permite que USER y ADMIN registren solicitudes.
- Exige autenticación para las demás rutas.
- Configura `formLogin` con `/login`, el parámetro `email`, el parámetro `password` y las rutas de éxito y error.
- Configura el cierre mediante POST `/logout`.
- Mantiene CSRF habilitado; no se utiliza `csrf.disable()` ni HTTP Basic ni JWT.

### Controlador y formulario de acceso

`LoginController` únicamente atiende GET `/login` y devuelve `auth/login`. El POST `/login` lo procesa el filtro de autenticación de Spring Security, no un método manual del controlador. El formulario utiliza `name="email"` y `name="password"`, que coinciden con la configuración.

### ¿Qué sucede al iniciar sesión?

Spring Security obtiene el usuario con `UserDetailsService`, compara la contraseña mediante `PasswordEncoder`, crea el objeto `Authentication` con sus autoridades y conserva el contexto autenticado mediante la sesión HTTP. El navegador envía la cookie de sesión en las siguientes peticiones. La cookie no contiene la contraseña.

### Ejercicio: seguir una petición protegida

Una persona anónima que abre `/pets/7/requests` pasa primero por la cadena de filtros de Spring Security. Como no tiene sesión autenticada, se redirige al login (302); `SupportRequestController` no debe crear un formulario de acceso porque esa responsabilidad pertenece a Spring Security.

Si después inicia sesión como USER y envía POST `/pets/7/requests/9/complete`, recibe 403 porque completar solicitudes es una operación administrativa. La autorización detiene la petición antes de ejecutar el controlador y el servicio.

### Cierre de sesión

El cierre se realiza con un formulario POST a `/logout`. Spring Security invalida la sesión y limpia el contexto. Un enlace GET que solamente redirige a `/login` no cierra correctamente la sesión.

## 5. Práctica 11.4 — Proteger operaciones mediante roles

No se reescribieron `PetService` ni `SupportRequestService`. Spring Security decide si la petición puede llegar al controlador; los servicios siguen validando la operación y el estado del dominio.

Ejemplo: para POST `/pets/7/requests/9/complete`, un USER recibe 403 y no llega al controlador. Un ADMIN puede continuar, pero el servicio solo permite completar si la solicitud está en estado `OPEN`. Si está `CANCELLED`, la regla de negocio sigue rechazando la transición y se presenta un conflicto 409.

### Página administrativa

Se agregó `AdminController` para GET `/admin` y la vista `templates/admin/home.html`. La página es mínima y sirve para comprobar visualmente la regla `hasRole("ADMIN")`.

### Preparar una cuenta ADMIN para desarrollo

El registro público siempre crea `USER`. Para realizar la prueba administrativa, registra una segunda cuenta y, solo en la base local de desarrollo, revisa la fila antes de modificarla:

```sql
SELECT id, email, role
FROM app_users
ORDER BY id;

UPDATE app_users
SET role = 'ADMIN'
WHERE email = 'correo-de-prueba@example.com'
  AND role = 'USER';

SELECT id, email, role
FROM app_users
WHERE email = 'correo-de-prueba@example.com';
```

Se debe reemplazar el correo por el de la cuenta de prueba y verificar la fila antes de ejecutar el `UPDATE`. Después hay que cerrar sesión e iniciar nuevamente para cargar la autoridad nueva. Este procedimiento es únicamente para pruebas locales, no para administrar permisos en producción.

### Ejercicio: cambiar temporalmente una regla

Si `/admin` se cambia de `hasRole("ADMIN")` a `authenticated()`, una cuenta USER podrá entrar. La política queda vulnerada porque la regla ya no exige el rol administrador. La regla original debe restaurarse inmediatamente.

### Limitación de propiedad de datos

No existe relación entre `SupportRequest` y `AppUser`. Por eso los roles no garantizan que cada persona vea solo sus solicitudes. Para aplicar propiedad se necesitaría relacionar el recurso con la cuenta que lo creó, validar esa relación en el servicio/repositorio y probar que cambiar identificadores en la URL no permita acceder a datos ajenos.

## 6. Práctica 11.5 — Formularios, navegación y protección CSRF

### Formulario de registro

Se agregó `templates/auth/register.html` con Thymeleaf y Bean Validation. Los campos de contraseña tienen `name` pero no usan `th:field`, para que el navegador no vuelva a recibir el valor de la contraseña si ocurre un error. La validación del servidor sigue siendo obligatoria aunque existan restricciones HTML.

### Protección CSRF

CSRF significa Cross-Site Request Forgery. Es un intento de hacer que el navegador de una persona autenticada envíe una petición no deseada aprovechando su sesión. El token CSRF permite comprobar que una petición que modifica datos contiene un valor asociado al contexto legítimo de la aplicación.

El token no demuestra el rol, no sustituye la autenticación, no reemplaza la autorización y tampoco sustituye la validación de los datos. Los formularios Thymeleaf que utilizan `th:action` y método POST reciben el campo oculto CSRF mediante la integración con Spring Security. No se debe escribir un token fijo en el HTML.

### Navegación por estado de sesión

`NavigationModelAdvice` agrega `currentEmail` e `isAdmin` al modelo. La navegación muestra:

- visitante: Inicio, Acerca de, Iniciar sesión y Registrarse;
- USER: Mascotas, Mi perfil, correo y Cerrar sesión;
- ADMIN: los enlaces anteriores y Administración.

`th:if` mejora la interfaz, pero no reemplaza las reglas de servidor de `SecurityConfig`.

### Formularios existentes

Se conservaron los formularios de mascotas y solicitudes. Los formularios que modifican datos utilizan `th:action` y POST para que Thymeleaf pueda incorporar el token. Se ocultaron los controles de creación, edición y eliminación de mascotas y los controles de completar, cancelar y reemplazar solicitudes cuando el usuario no es ADMIN. USER conserva la posibilidad de registrar solicitudes.

### GET no debe modificar datos

GET se utiliza para consultar o mostrar formularios. Las operaciones que crean, actualizan, eliminan, completan, cancelan o reemplazan información se envían mediante POST en las rutas del laboratorio. Esto evita que una visita o un enlace de lectura ejecute una modificación.

### Comprobación con las herramientas del navegador

1. En la pestaña Red/Network, enviar el formulario de login y revisar POST `/login` y su respuesta de redirección.
2. En Almacenamiento/Aplicación, comprobar que existe una cookie de sesión; no copiarla ni compartirla.
3. Inspeccionar los formularios POST renderizados y comprobar que contienen el campo oculto `_csrf`.
4. Cerrar sesión y volver a solicitar `/pets`; debe producirse una redirección al login.
5. Como USER, intentar una operación administrativa y comprobar que el servidor responde 403.

### Ejercicio: analizar respuestas

1. **302 al abrir `/pets` sin sesión:** falta autenticación; se debe iniciar sesión.
2. **403 al enviar un POST como USER a una operación ADMIN:** la autenticación existe, pero el rol no tiene permiso.
3. **403 al enviar POST como ADMIN sin token:** el rol sí es suficiente, pero CSRF rechaza la petición. Se debe corregir el formulario o incluir un token válido; no desactivar CSRF.

Si un POST autorizado llega al servicio y una transición no es válida, puede responder 409. Ese caso no es un fallo de autenticación ni de CSRF: es una regla de negocio.

## 7. Práctica 11.6 — Pruebas, depuración y reto integrador

### Pruebas agregadas

| Prueba | Qué comprueba |
|---|---|
| `RegistrationServiceTests` | Correo normalizado, contraseña codificada, rol USER, duplicados y confirmación. |
| `DatabaseUserDetailsServiceTests` | Conversión de la cuenta a `UserDetails`, autoridad y correo. |
| `AppUserRepositoryTests` | Persistencia y búsqueda por correo. |
| `SecurityRoutesIntegrationTests` | Rutas públicas, redirección anónima, rol ADMIN, prohibición a USER y CSRF. |
| `LoginIntegrationTests` | Inicio de sesión con contraseña correcta e incorrecta contra una cuenta de H2. |
| `RegistrationControllerIntegrationTests` | Registro HTTP, rol USER, hash y rechazo de POST sin CSRF. |
| `ProfileControllerIntegrationTests` | Redirección del visitante y vista del perfil autenticado. |

También se adaptaron las pruebas MVC existentes de mascotas y solicitudes: ahora utilizan usuarios simulados con el rol correspondiente y token CSRF en las operaciones POST, de modo que puedan seguir comprobando las reglas de negocio del Laboratorio 10.

Las pruebas utilizan H2 de `src/test/resources/application.yaml`; no deben conectarse a MariaDB de desarrollo.

### Diferencia entre usuarios simulados y login real

`.with(user(...).roles(...))` simula una identidad autenticada y permite probar la política de rutas. No comprueba la contraseña guardada. `LoginIntegrationTests` utiliza una cuenta persistida en H2 y el proceso real de autenticación para verificar que el correo y la contraseña coinciden.

### Diagnóstico de errores

| Síntoma | Qué revisar |
|---|---|
| Error de compilación | Nombres de clases, imports, dependencias y versión de Spring Boot. |
| `NoSuchBeanDefinitionException` | Que el componente exista y esté dentro del paquete escaneado. |
| 302 inesperado | Si la prueba necesita usuario autenticado. |
| 403 inesperado en POST | Rol, token CSRF y orden de los matchers. |
| 404 | Existencia del recurso y coincidencia de ruta. |
| 409 | Regla de negocio o transición de estado. |
| Tabla `APP_USERS` inexistente | Configuración H2 y mapeo JPA. |
| Login redirige a `?error` | Correo normalizado, hash, nombres de campos y estado `enabled`. |
| Funciona en H2 y falla en MariaDB | Diferencias de configuración entre ambas bases. |

### Laboratorio de errores: respuestas

- **Caso A — Contraseña en texto plano:** una copia de la base expondría directamente las contraseñas. Además, el proceso de login espera un hash codificado y no puede verificar correctamente una contraseña guardada como texto sin procesar. La solución es guardar `passwordEncoder.encode(...)`.
- **Caso B — Rol editable:** el usuario podría registrarse como ADMIN y elevar privilegios. Se elimina el campo del formulario y se asigna `USER` dentro del constructor de `AppUser`.
- **Caso C — POST `/login` responde 403:** primero se revisa CSRF, `th:action` y el token. No se empieza depurando la contraseña si el filtro CSRF ya rechazó la petición.
- **Caso D — La base indica ADMIN, pero el navegador recibe 403:** se debe cerrar sesión y autenticar de nuevo; la sesión anterior puede conservar las autoridades previas. También se comprueba que el adaptador genere `ROLE_ADMIN`.
- **Caso E — USER consulta mascotas, pero no completa solicitudes:** es el comportamiento esperado; completar requiere ADMIN.
- **Caso F — Desaparece el botón, pero POST sigue funcionando:** la interfaz solo oculta el control. La ruta necesita una regla del lado del servidor.
- **Caso G — Fallan pruebas MVC después de activar seguridad:** se agregan usuarios simulados y CSRF a las pruebas que deben llegar al controlador; se conservan pruebas anónimas para comprobar redirecciones y denegaciones.
- **Caso H — Acceso a solicitudes de otras personas:** falta la relación de propiedad entre `SupportRequest` y `AppUser`. El rol USER no demuestra que un registro pertenezca a esa persona.

### Reto integrador: perfil autenticado

Se agregó GET `/profile`. El controlador obtiene el correo desde `Authentication.getName()` y no recibe un `userId` del navegador. `ProfileService` recupera la cuenta mediante `AppUserRepository.findByEmail(...)` y devuelve un DTO `ProfileView` con correo, nombre visible y rol.

La vista `profile.html` presenta únicamente esos tres datos. No recibe ni muestra `passwordHash`. Si la cuenta ya no existe, `ProfileService` lanza `ResourceNotFoundException` y el manejador existente presenta un error 404 controlado.

**Respuestas de análisis del reto:**

1. Spring Security obtiene el correo del usuario autenticado y lo expone en `Authentication.getName()`.
2. `ProfileService` recupera la entidad `AppUser` mediante `AppUserRepository`.
3. El controlador no debe aceptar un `userId` libre porque alguien podría cambiarlo para consultar el perfil de otra cuenta.
4. Si el registro fue eliminado, la consulta no encuentra la cuenta y se produce un error controlado.
5. Thymeleaf recibe correo, nombre y rol. El hash de contraseña permanece en el servidor y no forma parte del DTO.
6. MockMvc comprueba que GET `/profile` sin usuario recibe una redirección al login.
7. MockMvc comprueba que un usuario con rol USER no puede entrar a `/admin` ni ejecutar operaciones administrativas.

## 8. Verificación final: respuestas

1. **¿Por qué `RegistrationController` no implementa el inicio de sesión?** Porque registra la cuenta; Spring Security procesa POST `/login`, verifica credenciales y crea el contexto autenticado.
2. **¿Qué contrato implementa `DatabaseUserDetailsService`?** `UserDetailsService`; devuelve el correo, hash codificado, autoridades y estado habilitado.
3. **¿Qué diferencia hay entre `encode` y `matches`?** `encode` genera el valor almacenado; `matches` verifica una contraseña ingresada frente al hash existente.
4. **¿Por qué el rol no debe venir del registro público?** Para impedir que un visitante se otorgue permisos de administrador.
5. **¿Qué cambia al activar CSRF?** Los formularios POST deben enviar un token válido; Thymeleaf lo incorpora al usar `th:action`.
6. **¿Por qué una sesión no autoriza a eliminar mascotas?** La sesión demuestra autenticación; la regla de ruta exige además ADMIN.
7. **¿Cómo distinguir 302, 403 y 409?** 302 redirige al login por falta de autenticación; 403 deniega por rol o CSRF; 409 indica una regla de negocio incumplida.
8. **¿Qué falta para que cada usuario acceda solo a sus registros?** Asociar las mascotas/solicitudes con la cuenta propietaria y validar esa relación en las consultas y servicios.

## 9. Ejecución del proyecto

Configura las variables de entorno de MariaDB antes de ejecutar la aplicación:

```text
DB_URL=jdbc:mariadb://localhost:3306/petmatch_lab
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
```

No guardes las credenciales reales en el repositorio. En Windows PowerShell ejecuta:

```powershell
$env:DB_URL="jdbc:mariadb://localhost:3306/petmatch_lab"
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

En Git Bash o Linux/macOS:

```bash
export DB_URL="jdbc:mariadb://localhost:3306/petmatch_lab"
export DB_USERNAME="tu_usuario"
export DB_PASSWORD="tu_contraseña"
./mvnw test
./mvnw spring-boot:run
```

La base de pruebas se configura separadamente con H2 en `src/test/resources/application.yaml`.


