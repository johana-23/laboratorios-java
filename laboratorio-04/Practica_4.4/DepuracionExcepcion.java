
public class DepuracionExcepcion {

    public static void main(String[] args) {
        
        try {
            try {
                int opcion = Integer.parseInt("hola");
                System.out.println(opcion);
            } catch (ArithmeticException e) {
                System.out.println("Entrada incorrecta.");
            }
        } catch (NumberFormatException e) {
            System.out.println("El catch de ArithmeticException NO atendió el error: escapó NumberFormatException.");
        }

        // Versión corregida
        try {
            int opcion = Integer.parseInt("hola");
            System.out.println(opcion);
        } catch (NumberFormatException e) {
            System.out.println("Entrada incorrecta (corregido).");
        }
    }
    /**
         * Depuración — Excepción incorrecta.
         * parseInt("hola") lanza NumberFormatException. Un catch (ArithmeticException) solo atiende
         * errores aritméticos (como dividir enteros entre cero), así que NO atrapa este error y el
         * programa se interrumpiría. Hay que capturar el tipo de excepción que realmente ocurre.
         */
}
