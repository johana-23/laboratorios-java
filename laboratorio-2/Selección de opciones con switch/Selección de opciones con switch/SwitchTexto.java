import java.util.Scanner;

public class SwitchTexto {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un comando (guardar, buscar, salir): ");
        String comando = scanner.nextLine();

        switch (comando.toLowerCase()) {
            case "guardar" -> System.out.println("Guardando...");
            case "buscar" -> System.out.println("Buscando...");
            case "salir" -> System.out.println("Saliendo...");
            default -> System.out.println("Comando desconocido.");
        }

        scanner.close();
    }
}