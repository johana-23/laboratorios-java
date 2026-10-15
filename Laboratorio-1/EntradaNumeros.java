import java.util.Scanner;

public class EntradaNumeros {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        
        System.out.print("1. Ingrese un número entero (ejemplo: cantidad de productos): ");
        int numeroEntero = scanner.nextInt();

        
        System.out.print("2. Ingrese un número decimal (ejemplo: precio o nota): ");
        // Recuerda: Si tu sistema está en español, debes usar coma (ejemplo: 19,5)
        double numeroDecimal = scanner.nextDouble();

        
        System.out.print("3. Ingrese el porcentaje de descuento (ejemplo: 0,10 o 0.10): ");
        double porcentaje = scanner.nextDouble();

       
        double resultadoProcesado = (numeroEntero * numeroDecimal) * (1 - porcentaje);

       
        System.out.println("\n--- RESULTADOS LEÍDOS ---");
        System.out.println("Valor entero capturado: " + numeroEntero);
        System.out.println("Valor decimal capturado: " + numeroDecimal);
        System.out.println("Porcentaje capturado: " + porcentaje);
        System.out.println("Resultado del cálculo de prueba: " + resultadoProcesado);

        
        scanner.close();
    }
}