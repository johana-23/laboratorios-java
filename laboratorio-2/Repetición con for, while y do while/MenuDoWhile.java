import java.util.Scanner;

public class MenuDoWhile {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        // El bloque do se ejecuta al menos una vez
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Continuar");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();

            if (opcion == 1) {
                System.out.println("Ejecutando acción...");
            } else if (opcion != 0) {
                System.out.println("Opción no válida.");
            }

        } while (opcion != 0); // La condición se evalúa al FINAL del ciclo

        System.out.println("Programa finalizado.");
        scanner.close();
    }
}