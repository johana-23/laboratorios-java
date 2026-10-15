import java.util.Scanner;


public class CalculadoraSegura {

   
    private static Double leerNumero(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine();
        try {
            double valor = Double.parseDouble(entrada.trim());
            if (!Double.isFinite(valor)) {
                System.out.println("Entrada inválida: NaN e Infinity no se aceptan.");
                return null;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida: debes escribir un número.");
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("1. Suma");
            System.out.println("2. Resta");
            System.out.println("3. Multiplicación");
            System.out.println("4. División");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String entrada = scanner.nextLine();

            int opcion;
            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: la opción debe ser numérica.");
                continue;
            }

            if (opcion == 0) {
                System.out.println("Hasta pronto.");
                salir = true;
                continue;
            }
            if (opcion < 1 || opcion > 4) {
                System.out.println("La opción no existe.");
                continue;
            }

            Double a = leerNumero(scanner, "Primer número: ");
            if (a == null) {
                continue;
            }
            Double b = leerNumero(scanner, "Segundo número: ");
            if (b == null) {
                continue;
            }

            switch (opcion) {
                case 1 -> System.out.println("Resultado: " + (a + b));
                case 2 -> System.out.println("Resultado: " + (a - b));
                case 3 -> System.out.println("Resultado: " + (a * b));
                case 4 -> {
                    if (b == 0) {
                        System.out.println("Operación no permitida: no se puede dividir entre cero.");
                    } else {
                        System.out.println("Resultado: " + (a / b));
                    }
                }
            }
        }

        scanner.close();
    }
}
