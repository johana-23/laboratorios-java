import java.util.Scanner;

public class DepuracionSwitch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- MENÚ DE PRUEBA ---");
        System.out.println("1. Opción A");
        System.out.println("2. Opción B");
        System.out.println("3. Opción C");
        System.out.print("Ingrese una opción (1-3): ");

        int opcion = scanner.nextInt();

        // switch sin default (temporalmente)
        switch (opcion) {
            case 1 -> System.out.println("Elegiste Opción A");
            case 2 -> System.out.println("Elegiste Opción B");
            case 3 -> System.out.println("Elegiste Opción C");
        }

        System.out.println("Fin del programa.");
        scanner.close();
    }
}