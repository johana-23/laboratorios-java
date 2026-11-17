public class MethodStack {

    public static void main(String[] args) {
        int initialValue = 4;
        int result = calculate(initialValue);

        System.out.println("Final result: " + result);
    }

    static int calculate(int value) {
        int doubled = doubleValue(value);
        return doubled + 3;
    }

    static int doubleValue(int number) {
        return number * 2;
    }

}

/* ¿Qué valor tiene number? 4
¿Qué otros métodos todavía están esperando el retorno?
calculate
¿Cuánto devuelve doubleValue? 8
¿Qué se guardará finalmente en result? 11 

¿ya terminó calculatemientras estabas detenido dentro de doubleValue? ¿Por qué?
porque calculate todavía está esperando el valor de retorno de doubleValue para poder continuar con su ejecución y completar la operación de suma.


*/    