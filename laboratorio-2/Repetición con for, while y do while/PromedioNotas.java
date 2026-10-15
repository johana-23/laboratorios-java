import java.text.DecimalFormat;
import java.util.Scanner;

public class PromedioNotas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#.##");

        
        System.out.print("Cantidad de notas: ");
        int cantidad = scanner.nextInt();

        double suma = 0; 

        
        for (int i = 1; i <= cantidad; i++) {
            System.out.print("Nota " + i + ": ");
            double nota = scanner.nextDouble();
            suma += nota;
        }

        
        if (cantidad > 0) {
            double promedio = suma / cantidad;
            System.out.println("Promedio: " + df.format(promedio));
        } else {
            System.out.println("La cantidad de notas debe ser mayor a cero.");
        }

        scanner.close();
    }
}