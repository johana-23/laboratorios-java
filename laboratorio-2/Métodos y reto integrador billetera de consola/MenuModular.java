import java.util.Scanner;

public class MenuModular {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            
            mostrarMenu();

            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            
            switch (opcion) {
                case 1 -> System.out.println("-> Opción elegida: Registrar ingreso");
                case 2 -> System.out.println("-> Opción elegida: Registrar gasto");
                case 3 -> System.out.println("-> Opción elegida: Consultar saldo");
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    
    static void mostrarMenu() {
        System.out.println();
        System.out.println("1. Registrar ingreso");
        System.out.println("2. Registrar gasto");
        System.out.println("3. Consultar saldo");
        System.out.println("0. Salir");
    }
}