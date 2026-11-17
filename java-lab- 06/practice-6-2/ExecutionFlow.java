
public class ExecutionFlow {

    public static void main(String[] args) {
        int base = 7;
        int bonus = 8;

        int total = add(base, bonus);

        System.out.println("Total: " + total);
    }

    static int add(int first, int second) {
        return first + second;
    }
}

/* Ejercicio — ¿Por qué no cambió la salida?
porque el código fuente no se recompiló después de modificar la variable bonus. La clase ExecutionFlow.class sigue utilizando el valor original de bonus (5) que estaba presente en la compilación anterior.

Modifica bonusde 5a 8 sin recompilar .
Ejecuta nuevamente java -cp out ExecutionFlow. ¿Qué observas?
que sigue mostrando en consola el total como 12.
Vuelve a ejecutar javac -d out ExecutionFlow.java.
Ejecuta la clase otra vez.
Explique por qué el resultado de la aplicación compilada cambia después de actualizar el .class.
cambia porque recompile el codigo fuente con el nuevo valor de la variable bonus (8), y al ejecutar la clase compilada, ahora utiliza el valor actualizado, resultando en un total diferente.
*/