import java.util.Scanner;

public class MontoSeguro {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean valido = false;

        while (!valido) {
            System.out.print("Monto: ");
            String entrada = scanner.nextLine();

            try {
                double monto = Double.parseDouble(entrada.trim());

                if (!Double.isFinite(monto)) {
                    System.out.println("NaN e Infinity no son montos válidos.");
                } else if (monto == 0) {
                    System.out.println("El monto no puede ser cero.");
                } else if (monto < 0) {
                    System.out.println("El monto no puede ser negativo.");
                } else {
                    System.out.println("Monto aceptado: $" + monto);
                    valido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un número.");
            }
        }

        scanner.close();
    }
}
