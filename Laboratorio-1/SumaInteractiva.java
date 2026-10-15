import java.util.Scanner;

public class SumaInteractiva {

    public static void main(String[] args) {
        // 1. Crear el objeto Scanner para leer desde la consola
        Scanner scanner = new Scanner(System.in);

        // 2. Solicitar los dos números enteros
        System.out.print("Ingrese el primer número entero: ");
        int numero1 = scanner.nextInt();

        System.out.print("Ingrese el segundo número entero: ");
        int numero2 = scanner.nextInt();

        // 3. Realizar los cálculos guardando cada resultado en una variable
        int suma = numero1 + numero2;
        int resta = numero1 - numero2;
        int multiplicacion = numero1 * numero2;
        
        // La división entre enteros ('int') descarta los decimales (división entera)
        int division = numero1 / numero2; 
        
        // El operador '%' obtiene el residuo o módulo de la división
        int residuo = numero1 % numero2; 

        // 4. Mostrar los resultados
        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Suma: " + suma);
        System.out.println("Resta: " + resta);
        System.out.println("Multiplicación: " + multiplicacion);
        System.out.println("División (entera): " + division);
        System.out.println("Residuo (%): " + residuo);

        scanner.close();
    }
}