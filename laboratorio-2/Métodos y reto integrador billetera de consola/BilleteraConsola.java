
import java.util.Scanner;

public class BilleteraConsola {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nombre del titular: ");
        String titular = scanner.nextLine();

        double saldo = 0;
        int opcion;

        do {
            mostrarMenu();

            System.out.print("Opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> {
                    // Llamamos al método y actualizamos la variable saldo
                    saldo = registrarIngreso(scanner, saldo);
                }
                case 2 -> {
                    // Llamamos al método y actualizamos la variable saldo
                    saldo = registrarGasto(scanner, saldo);
                }
                case 3 -> {
                    // Muestra el saldo actual
                    mostrarSaldo(saldo);
                }
                case 4 -> {
                    // Muestra el resumen con titular y saldo
                    mostrarResumen(titular, saldo);
                }
                case 0 -> System.out.println("Programa finalizado.");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    // ==========================================
    // MÉTODOS DE LA APLICACIÓN
    // ==========================================

    static void mostrarMenu() {
        System.out.println();
        System.out.println("=========================");
        System.out.println("       MI BILLETERA");
        System.out.println("=========================");
        System.out.println("1. Registrar ingreso");
        System.out.println("2. Registrar gasto");
        System.out.println("3. Consultar saldo");
        System.out.println("4. Mostrar resumen");
        System.out.println("0. Salir");
    }

    // Registra un ingreso si es mayor a cero; de lo contrario, devuelve el saldo intacto
    static double registrarIngreso(Scanner scanner, double saldo) {
        System.out.print("Valor: ");
        double ingreso = scanner.nextDouble();

        if (ingreso > 0) {
            saldo += ingreso;
            System.out.println("Acumulado: " + saldo);
        } else {
            System.out.println("El ingreso debe ser mayor a cero.");
        }

        return saldo;
    }

    // Registra un gasto si es mayor a cero y no excede el saldo disponible
    static double registrarGasto(Scanner scanner, double saldo) {
        System.out.print("Valor: ");
        double gasto = scanner.nextDouble();

        if (gasto <= 0) {
            System.out.println("El gasto debe ser mayor a cero.");
        } else if (gasto > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            saldo -= gasto;
            System.out.println("Nuevo saldo: " + saldo);
        }

        return saldo;
    }

    // Muestra únicamente el saldo actual
    static void mostrarSaldo(double saldo) {
        System.out.println("Saldo: " + saldo);
    }

    // Muestra los datos generales de la cuenta
    static void mostrarResumen(String titular, double saldo) {
        System.out.println("Titular: " + titular);
        System.out.println("Saldo: " + saldo);
    }
}