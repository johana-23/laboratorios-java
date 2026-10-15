import java.util.Scanner;

public class RepetirHastaCero {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Lectura inicial (Centinela)
        System.out.print("Ingrese un número (0 para salir): ");
        int numero = scanner.nextInt();

        // 2. Condición: Se repite MIENTRAS sea diferente de 0
        while (numero != 0) {
            System.out.println("Número ingresado: " + numero);

            // Se vuelve a solicitar el dato dentro del ciclo
            System.out.print("Ingrese otro número (0 para salir): ");
            numero = scanner.nextInt();
        }

        System.out.println("Ha ingresado 0. Programa finalizado.");
        scanner.close();
    }
}