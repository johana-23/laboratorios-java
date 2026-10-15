import java.util.Scanner;
import java.util.Locale;

public class ConversorTemperatura {

    public static void main(String[] args) {
        // Usamos Locale.US para ingresar los decimales con punto (.) en la consola
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        // 1. Solicitud de la temperatura en Celsius
        System.out.print("Temperatura en °C: ");
        double celsius = scanner.nextDouble();

        // 2. Cálculo de la equivalencia en Fahrenheit
        // Usamos 9.0 y 5.0 para asegurar operaciones con decimales (double)
        double fahrenheit = (celsius * 9.0 / 5.0) + 32.0;

        // 3. Muestra del resultado
        System.out.println(celsius + " °C equivalen a " + fahrenheit + " °F");

        scanner.close();
    }
}