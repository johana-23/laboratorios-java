public class ExecutionFlowError {

    public static void main(String[] args) {
        int base = 7;
        int bonus = 8;

        int total = add(base, bonus);

        System.out.println("Total: " + total);
    }

    static int add(int first, int second) {
        return first - second;
    }
}

/* ¿Qué componente intenta encontrar esa clase?
intenta encontrar la clase MissingClass que no está presente en el en la carpeta donde buscaria el archivo de la clase. lo que provoca un error de ejecución.

¿Por qué no se escribe la extensión al indicar una clase compilada?
porque el nombre de la clase compilada incluye la extensión .class, pero al ejecutar el comando java, no se necesita especificar la extensión.

Explica qué representan .java, el .java refiere al código fuente escrito en el lenguaje de programación Java en escrito en el archivo, mientras que .clas El bytecode es un conjunto de instrucciones que la máquina virtual de Java (JVM) puede interpretar y ejecutar.
 .class,s representa el archivo compilado que contiene el bytecode generado a partir del código fuente.
  javac,  compila el código fuente de Java (.java) en bytecode (.class) que puede ser ejecutado por la JVM.
  java, java, ejecuta la clase compilada (.class) en la JVM, interpretando y ejecutando el bytecode.
   -cp,  especifica el classpath, que es la ruta donde la JVM busca las clases compiladas para ejecutar. 
  javap, es una herramienta que permite inspeccionar el contenido de los archivos .class, mostrando información sobre los métodos, campos y bytecode de la clase.
   bytecode es un conjunto de instrucciones intermedias que la JVM puede interpretar y ejecutar. Es el resultado de compilar el código fuente de Java y es independiente de la plataforma, lo que permite que el mismo bytecode se ejecute en diferentes sistemas operativos y arquitecturas. 
   JIT (Just-In-Time) es un compilador que convierte el bytecode en código máquina nativo en tiempo de ejecución, mejorando el rendimiento de la aplicación al ejecutar el código directamente en la CPU del sistema.
   . Luego señala en qué etapa apareció cada error del ejercicio.
   en la etapa de ejecución, ya que el error se produce cuando se intenta ejecutar la clase MissingClass que no está presente en el classpath, lo que provoca un error de ejecución, tambien cuando intente compilar el codigo fuente con una clase ya compilada previamente, y otro error cuando intente complilar el codigo con un error en el código que lo genere y me mostro donde estaba el error y que faltaba para su correcion . 
   */