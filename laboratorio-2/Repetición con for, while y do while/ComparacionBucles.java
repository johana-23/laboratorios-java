import java.util.Scanner;

public class ComparacionBucles {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
       
        System.out.println("--- MENÚ CON WHILE ---");
        int opcionWhile = -1; // Valor inicial arbitrario para poder "forzar" la primera entrada
        
        while (opcionWhile != 0) {
            System.out.println("1. Saludar");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcionWhile = scanner.nextInt();

            if (opcionWhile == 1) {
                System.out.println("¡Hola desde el bucle while!");
            }
        }

        
        System.out.println("\n--- MENÚ CON DO-WHILE ---");
        int opcionDoWhile;

        do {
            System.out.println("1. Saludar");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcionDoWhile = scanner.nextInt();

            if (opcionDoWhile == 1) {
                System.out.println("¡Hola desde el bucle do-while!");
            }
        } while (opcionDoWhile != 0);

        scanner.close();
    }
}

