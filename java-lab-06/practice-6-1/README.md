*¿Qué comando utilizarías para saber si puedes compilar ? 
//Utilizaría el comando javac -version, porque permite consultar la versión del compilador de Java instalado en el sistema.

*¿Cuál sirve para iniciar un programa? 
//Utilizaría el comando java NombreClase para ejecutar una clase compilada

*¿Por qué disponer de java no garantiza disponer de javac? 
//Porque los dos cumplen funciones diferentes. java permite ejecutar programas Java, mientras que javac es el compilador que transforma el código fuente de un archivo .java en bytecode, normalmente almacenado en un archivo .class.


*Si ya recibes un .classcompatible con las dependencias necesarias, ¿necesitas un compilador para iniciar esa clase?
//No, porque el archivo .class ya contiene el bytecode generado durante la compilación.


*¿Qué herramienta permite examinar el bytecode? JVM
//Utilizaría la herramienta javap, que permite examinar información de una clase compilada, como sus métodos, atributos y, con las opciones adecuadas, sus instrucciones de bytecode.

El comando sería javap -c NombreClase. La opción -c muestra las instrucciones de bytecode de los métodos de la clase.

*Reto — Explica el error
Un compañero afirma: «Instalé una JVM, por lo tanto puedo desarrollar y compilar cualquier proyecto Java».

Explique por qué esa conclusión no está justificada. Utiliza correctamente JDK , compilador y JVM .

//Esta afirmación no es correcta porque una JVM no incluye necesariamente todas las herramientas que se necesitan para desarrollar y compilar programas Java.
- JDK (Java Development Kit): es el kit de desarrollo que incluye herramientas para programar y compilar aplicaciones Java, como el compilador javac. También incluye componentes necesarios para ejecutar programas.
- Compilador (javac): transforma el código fuente escrito en archivos .java en bytecode, que normalmente se guarda en archivos .class.
- JVM (Java Virtual Machine): es la máquina virtual que ejecuta el bytecode de Java.

*Comprobación — ¿Qué hace cada herramienta?
//java
Sirve para ejecutar programas Java mediante la JVM. 
javac
Sirve para compilar el código fuente de Java. 
javap
Sirve para examinar una clase compilada.

*¿Qué comprobaría en mi computador para trabajar con Java 21?
Para asegurarme de que mi computador está preparado para trabajar con Java 21, realizaría las siguientes comprobaciones:
1. Consultar la versión de Java instalada
Ejecutaría el comando:

java -version

Este comando muestra la versión del entorno de ejecución de Java que utiliza el sistema al ejecutar el comando. Comprobaría que corresponda a Java 21 o que la configuración utilizada por el proyecto sea la adecuada.

2. Comprobar si tengo disponible el compilador
Ejecutaría:

javac -version

Este comando permite consultar la versión del compilador. Si necesito desarrollar en Java 21, comprobaría que el compilador corresponda a esa versión.