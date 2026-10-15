import java.util.Scanner;

public class MenuSeguro {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("1. Consultar");
            System.out.println("2. Registrar");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String entrada = scanner.nextLine();

            try {
                int opcion = Integer.parseInt(entrada.trim());

                switch (opcion) {
                    case 1 -> System.out.println("Consultando...");
                    case 2 -> System.out.println("Registrando...");
                    case 0 -> {
                        System.out.println("Hasta pronto.");
                        salir = true;
                    }
                    default -> System.out.println("La opción no existe.");
                }
            } catch (NumberFormatException e) {
                System.out.println("La opción no es numérica.");
            }
        }

        scanner.close();
    }
}
