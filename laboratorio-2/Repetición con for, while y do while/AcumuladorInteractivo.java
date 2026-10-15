import java.text.DecimalFormat;
import java.util.Scanner;

public class AcumuladorInteractivo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#.##");

        double acumulado = 0;
        double valor;

        do {
            System.out.print("Valor: ");
            valor = scanner.nextDouble();

           
            if (valor != 0) {
                acumulado += valor;
                System.out.println("Acumulado: " + df.format(acumulado) + "\n");
            }

        } while (valor != 0);

        System.out.println("Programa finalizado.");
        scanner.close();
    }
}