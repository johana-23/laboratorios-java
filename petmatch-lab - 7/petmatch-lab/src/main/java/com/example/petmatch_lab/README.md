Definición de conceptos de PetMatch Mini
1. Paquete (Package)

Es una forma de organizar las clases de Java en grupos según su función. En nuestro proyecto tenemos paquetes como controller, model, repository y service, que permiten mantener el código organizado.

2. Clase (Class)

Es una estructura que permite definir atributos y métodos para representar algo o realizar una función. Por ejemplo, la clase Pet representa una mascota y contiene información como su ID, nombre, especie, edad y descripción.

3. Dependencia (Dependency)

Es un componente que una clase necesita para poder realizar su trabajo. Por ejemplo, PetController necesita PetService para consultar la información de las mascotas.

4. Maven

Es una herramienta que utilizamos para administrar las dependencias y construir el proyecto Java. También permite ejecutar pruebas y comprobar si la aplicación funciona correctamente.

5. Starter

Es un conjunto de dependencias que facilita agregar funcionalidades a un proyecto Spring Boot. Por ejemplo, spring-boot-starter-web proporciona las herramientas necesarias para crear aplicaciones web y manejar peticiones HTTP.

6. Bean

Es un objeto que Spring crea y administra dentro de la aplicación. En nuestro proyecto, PetController, PetService e InMemoryPetRepository son componentes administrados por Spring gracias a sus anotaciones.

7. Inyección de dependencias

Es un mecanismo que permite que Spring entregue a una clase los objetos que necesita, sin que tenga que crearlos directamente. Por ejemplo, Spring le entrega PetService a PetController mediante su constructor.

8. Controlador (Controller)

Es la clase encargada de recibir y atender las solicitudes del navegador. En nuestro proyecto, PetController maneja las peticiones relacionadas con las mascotas, como mostrar el catálogo y consultar sus detalles.

9. Ruta (Route)

Es la dirección que permite acceder a una funcionalidad de la aplicación. Por ejemplo, /pets muestra el catálogo de mascotas y /pets/2 permite consultar la mascota cuyo ID es 2.

10. Modelo de dominio (Domain Model)

Es la representación de los elementos principales del sistema y de la información que manejan. En PetMatch Mini, la clase Pet representa una mascota con sus atributos: ID, nombre, especie, edad y descripción.

11. Model de Spring MVC

Es un objeto que permite enviar información desde el controlador hacia la vista. Por ejemplo, utilizamos model.addAttribute("pet", pet) para enviar los datos de una mascota a la plantilla HTML.

12. Vista (View)

Es la parte de la aplicación encargada de presentar la información al usuario. En nuestro proyecto, las vistas son las páginas HTML que muestran el catálogo y los detalles de las mascotas.

13. Plantilla (Template)

Es un archivo HTML que contiene una estructura de presentación y expresiones de Thymeleaf para mostrar información dinámica. Por ejemplo, pets/detail.html muestra el nombre, la especie, la edad y la descripción de una mascota.

14. Fragmento (Fragment)

Es una parte reutilizable de una plantilla HTML que podemos incluir en diferentes páginas para evitar repetir código. En nuestro proyecto tenemos fragmentos para la cabecera y la navegación, ubicados en la carpeta templates/fragments.

15. Recurso estático (Static Resource)

Es un archivo que la aplicación entrega al navegador sin tener que generar su contenido dinámicamente. Por ejemplo, nuestro archivo application.css contiene los estilos visuales que utiliza PetMatch Mini.