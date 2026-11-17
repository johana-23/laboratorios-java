# Laboratorio 8 — Solución y respuestas

**Proyecto:** PetMatch Mini  
**Programa:** Análisis y Desarrollo de Software (ADSO) — SENA


## Práctica 8.1 — HTTP y formularios

### Antes de ejecutar — Predice

1. Se observan dos peticiones: GET `/pets/new` para mostrar el formulario y POST `/pets` para enviar sus datos.
2. La petición POST debe modificar la colección, siempre que los datos sean válidos. El GET solo presenta el formulario.
3. Los valores viajan como datos del formulario en el cuerpo de la petición POST, normalmente con el formato `application/x-www-form-urlencoded`. Las claves serán `name` y `species`.
4. Si el método no está asociado a esa ruta, Spring responde con un error HTTP de método no permitido (405), si la ruta existe con otro método.

### Ejercicio — Reconocer las partes

Si se cambia `name="species"` por `name="petType"`, el navegador envía la clave `petType`. Spring busca una propiedad compatible con ese nombre. Como el DTO tiene `species` y no `petType`, el valor no se asigna automáticamente a `species`.

**Conceptos:** `action` indica la ruta destino; `method` indica el método HTTP; `name` es la clave que viaja con el valor; `id` identifica el control en el HTML y puede relacionarlo con un `label`.

## Práctica 8.2 — DTO y enlace de formularios

### Pregunta: ¿por qué no recibir directamente un `Pet`?

Porque `Pet` representa una mascota que ya forma parte del catálogo y contiene su identificador. El formulario recibe datos que todavía pueden estar incompletos o ser incorrectos. `PetForm` separa esos datos de entrada del objeto de dominio y no permite que el usuario decida el ID.

**DTO** significa *Data Transfer Object* u objeto de transferencia de datos. `@ModelAttribute("petForm")` permite a Spring MVC enlazar los campos recibidos con un objeto `PetForm`. `th:object` selecciona ese objeto en la plantilla y `th:field` conecta cada control HTML con una de sus propiedades.

### Ejercicio — Sigue una propiedad

La propiedad `species` está declarada en `PetForm`; `getSpecies()` permite leerla y `setSpecies()` permite asignarle el valor recibido. En la plantilla, `th:field="*{species}"` enlaza el campo de entrada con esa propiedad. Cuando se envía el formulario, Spring utiliza el nombre del campo y el método setter para completar el objeto.

### Reto de presentación

Se agregó debajo del campo de edad una explicación breve: “Ingresa la edad en años. Debe ser un número entero igual o mayor que cero.” Es un texto de ayuda de la vista y no requiere agregar atributos a `Pet`.

### Depuración — Tres problemas comunes

1. Si se cambia `th:object="${petForm}"` por `th:object="${animalForm}"`, Thymeleaf no encuentra `animalForm` porque el controlador agrega al modelo la clave `petForm`.
2. `*{type}` intenta consultar una propiedad llamada `type`. Como `PetForm` no tiene esa propiedad ni sus métodos de acceso, se produce un error al resolver el enlace.
3. La plantilla necesita que el controlador agregue un objeto `petForm` al modelo. Sin él, Thymeleaf no tiene el objeto sobre el cual resolver `th:field`.

### Pregunta de conceptos

`Pet` representa el registro del catálogo; `PetForm` recibe y valida los datos del formulario. `th:object` selecciona el objeto de formulario y `th:field` enlaza cada control con una propiedad. Los valores provienen de los campos enviados por el navegador y Spring los convierte y asigna al DTO.

## Práctica 8.3 — Validación

`@Valid` solicita a Spring que ejecute las restricciones declaradas en `PetForm`, como `@NotBlank`, `@Size`, `@NotNull` y `@Min`. `BindingResult` recibe los errores de validación y de conversión para que el controlador pueda decidir qué respuesta mostrar.

### Antes de ejecutar — Predice

Con edad `-1`, el controlador debe devolver `pets/form` porque hay un error de validación. No debe redirigir al detalle ni llamar al servicio. Si hiciera una redirección, el objeto `PetForm` y los errores asociados no estarían disponibles automáticamente en la nueva petición, por lo que podrían perderse los datos válidos que la persona ya escribió.

### Depuración — Un error por vez

1. `BindingResult` debe estar inmediatamente después del parámetro al que corresponde (`PetForm`). Si se coloca un `Model` entre ellos, Spring no lo asocia correctamente con ese objeto y los errores no se gestionan como espera el controlador.
2. `th:errors="*{unknown}"` intenta consultar una propiedad llamada `unknown`, que no existe en `PetForm`; Thymeleaf no puede resolverla.
3. Sin `@Valid`, la existencia de las anotaciones en `PetForm` no hace que se ejecuten automáticamente en ese método. La validación declarada debe activarse en el flujo MVC.

### Conversión de edad

`age="abc"` no es lo mismo que una edad negativa. `@Min(0)` comprueba un valor numérico ya convertido; Spring MVC detecta antes el error al intentar convertir el texto `abc` a `Integer`. Ese error de enlace también queda disponible en `BindingResult`.

## Práctica 8.4 — Crear y almacenar

### Separación de responsabilidades

Agregar una mascota en el repositorio sin cambiar el controlador ni la plantilla demuestra que el repositorio administra los datos, el servicio coordina las operaciones, el controlador atiende las solicitudes y la plantilla presenta la información.

Al pulsar “Ver detalle” de Milo, el navegador genera un GET a `/pets/2` mediante el `th:href` de la tarjeta. `PetController` recibe el ID con `@PathVariable` y llama a `PetService`; el servicio consulta `InMemoryPetRepository`. La búsqueda devuelve un `Optional<Pet>`. Si existe, el controlador agrega la mascota al `Model`, Thymeleaf genera el HTML y el navegador muestra la respuesta.

Una lista es una colección de objetos Java; `th:each` repite una parte de la plantilla por cada objeto. `th:text` muestra un valor. `${...}` consulta datos del modelo y `@{...}` construye una URL. Una ruta desconocida no coincide con un controlador; una mascota inexistente sí llega a una ruta válida, pero el ID no está en el repositorio y se responde 404.

### Preguntas sobre el repositorio y el servicio

- `synchronized` evita que dos solicitudes ejecuten simultáneamente una operación crítica sobre la colección compartida.
- `nextId` comienza en `4L` porque las tres mascotas iniciales usan los IDs 1, 2 y 3.
- `Pet` no tiene setters porque sus atributos son `final`; para modificarla se construye otro objeto con los valores actualizados.
- Se valida en `PetForm` para dar respuesta al usuario en el formulario y en `PetService` para proteger las reglas aunque el servicio sea llamado desde otra clase Java.
- `strip()` elimina espacios en blanco al inicio y al final del texto.
- `create(...)` devuelve la instancia `Pet` que se acaba de construir y almacenar, con el ID asignado por el repositorio.
- `update(...)` devuelve `Optional<Pet>` porque puede no existir el ID que se intenta actualizar. `delete(...)` devuelve `boolean` para indicar si se eliminó un registro.

### Ejercicio — Registra y comprueba

Al registrar dos mascotas, el repositorio asigna IDs consecutivos diferentes. Si se agregan espacios al inicio y al final de la descripción, el servicio utiliza `strip()` y guarda el texto sin esos espacios externos.

### Laboratorio de errores

1. Si `nextId` se cambia a `2L`, `pets.put(2L, ...)` reemplaza el valor que ya estaba asociado a esa clave en el `Map`. Se perdería la referencia al registro anterior. Por eso el ID debe ser único y avanzar correctamente.
2. Si se omite `bindingResult.hasErrors()`, el controlador podría enviar datos incorrectos al servicio, provocar excepciones o intentar almacenar valores que no cumplen las reglas. También podría fallar la conversión de campos.
3. Si se devuelve `pets/detail` directamente, la plantilla necesita un atributo `pet` en el `Model` con el objeto recién creado. En el flujo implementado se redirige al detalle y el controlador vuelve a consultar el registro.
4. `findAll()` devuelve `List.copyOf(...)`, que es una copia no modificable. Para cambiar el catálogo se deben utilizar los métodos del repositorio y no modificar la lista que se entrega para consulta.

### Diferencia entre vista y redirección

Devolver `pets/detail` solicita renderizar esa plantilla en la petición actual. Devolver `redirect:/pets/ID` genera una nueva petición GET al detalle después del POST. Este patrón se conoce como Post/Redirect/Get y evita que una actualización de página repita directamente el envío anterior.

Un atributo flash es un dato temporal que Spring conserva para la siguiente petición, normalmente después de una redirección. Se usa, por ejemplo, para mostrar “Mascota registrada correctamente” sin agregar el mensaje a la URL.

## Práctica 8.5 — CRUD

CRUD significa **Create, Read, Update, Delete**: crear, leer, actualizar y eliminar. En PetMatch Mini corresponde a registrar una mascota, consultar el catálogo o un detalle, editar sus datos y eliminarla.

Los dos formularios de modificación utilizan POST porque las acciones cambian el estado del catálogo. GET se reserva para consultar información y mostrar formularios; no debe borrar ni actualizar registros.

Se vuelve a comprobar la existencia en POST porque el registro podría dejar de existir después de mostrar el formulario de edición. Si ya no existe, se responde 404 en vez de intentar actualizar un recurso inexistente.

### Ejercicio — Antes y después de editar

Al editar la mascota 2, el formulario se completa con sus datos actuales. Al cambiar solo la edad o descripción y enviar el POST, el controlador conserva el ID que viene en la ruta. El repositorio reemplaza el objeto asociado a la clave `2L`; no crea una clave nueva. Si la edad es negativa, se vuelve al formulario y se conservan los valores introducidos junto con los errores.

### Reto — Eliminar y comprobar

Después de eliminar correctamente una mascota, el repositorio devuelve `false` si se intenta eliminar el mismo ID por segunda vez, porque ya no existe. El controlador traduce ese resultado a HTTP 404. La ruta GET del detalle también responde 404 porque el repositorio ya no encuentra ese ID.

### Depuración

1. Si el controlador agrega `pet` pero la plantilla sigue usando `th:object="${petForm}"`, el nombre del atributo no coincide y Thymeleaf no encuentra el objeto esperado.
2. Si no se vuelve a agregar `editing=true` cuando hay errores de edición, la plantilla puede interpretar el formulario como uno de creación y enviar el POST a `/pets` en lugar de `/pets/{id}`.
3. Si se reemplaza la actualización por `pets.put(nextId++, updated)`, se crea una clave nueva y cambia el ID. Eso sería un alta, no una actualización del registro existente.
4. Si el formulario de eliminación usa GET, una acción que modifica datos podría ejecutarse al seguir un enlace, precargarlo o repetir una navegación. El controlador no debe implementar el borrado con GET; el proyecto usa un formulario POST.

## Práctica 8.6 — Pruebas y depuración

Las pruebas automatizadas complementan la comprobación manual porque se pueden repetir y verifican resultados concretos de forma consistente. Las pruebas unitarias del servicio crean un repositorio nuevo en cada preparación para que los datos de una prueba no afecten a otra.

`assertThrows(...)` comprueba que una operación lance una excepción del tipo esperado. En este proyecto se usa para verificar que el servicio rechaza una edad negativa cuando se llama directamente desde Java y que el catálogo no aumenta.

`MockMvc` permite simular peticiones HTTP a Spring MVC y comprobar códigos de estado, vistas, atributos del modelo y redirecciones sin abrir el navegador. Al revisar una prueba fallida, primero se identifica qué esperaba, qué obtuvo y en qué capa está el problema; no se debe cambiar el HTML si el fallo está en el repositorio o el servicio.

## Reto integrador — Búsqueda por especie

Se implementó la búsqueda por especie sin modificar el flujo CRUD:

- `pets/list.html` contiene un formulario GET con el campo `species`.
- `PetController.list(...)` recibe el parámetro opcional `species`.
- `PetService.findBySpecies(...)` realiza el filtrado, sin poner reglas de negocio en el HTML.
- Si no se envía una especie, se muestra el catálogo completo.
- La comparación no distingue mayúsculas y minúsculas.
- Si no hay coincidencias, la vista muestra un estado vacío y permite volver al catálogo.
- Se añadieron pruebas del servicio y pruebas MVC para una especie existente (`Gato`) y una búsqueda sin resultados (`Ave`).

La búsqueda utiliza GET porque solo consulta información y no modifica el catálogo.

## Diagnóstico final

**Caso A — Registro válido (Mora, Gato, 2):** Spring MVC recibe el POST y enlaza los campos con `PetForm`. `@Valid` ejecuta las restricciones y `BindingResult` confirma que no hay errores. El controlador llama a `PetService.create(...)`; el servicio normaliza y verifica los datos. `InMemoryPetRepository` asigna el siguiente ID, crea el objeto `Pet` y lo almacena. El controlador redirige a `/pets/{id}` y el GET posterior muestra el detalle.

**Caso B — Registro inválido (nombre vacío y edad -2):** el controlador devuelve `pets/form`. `BindingResult` contiene errores, por lo que no se ejecuta `PetService.create(...)`. La cantidad de mascotas no cambia porque no se llama al repositorio para crear un registro.

**Caso C — Edición del ID 2:** conserva el mismo ID porque el controlador lo obtiene de la ruta `/pets/2`. El repositorio reemplaza el valor de la clave `2L` con un nuevo objeto que conserva ese ID. No se inserta otra clave porque se trata de actualizar el mismo registro.

**Caso D — Eliminación repetida del ID 4:** el segundo intento devuelve `false` porque `Map.remove(4L)` no encuentra ningún valor que eliminar. El controlador convierte ese resultado en 404 Not Found. `GET /pets/4` también devuelve 404 porque el detalle intenta buscar un registro que ya no existe.

**Caso E — `age="abc"`:** no es lo mismo que `@Min(0)`. `@Min` valida un número ya convertido y comprueba que no sea negativo. La conversión de texto a `Integer` falla antes, en el enlace de datos de Spring MVC; el error queda registrado en `BindingResult`.

## Comprobación final

Desde la carpeta del proyecto ejecuta en Windows PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```