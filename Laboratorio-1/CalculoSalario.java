// import java.util.Scanner;
// import java.util.Locale;

// public class CalculoSalario {

//     public static void main(String[] args) {
//         // Usamos Locale.US para aceptar decimales con punto (.) en la consola
//         Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

//         // Entrada de datos desde la terminal
//         System.out.print("Nombre del trabajador: ");
//         String nombre = scanner.nextLine();

//         System.out.print("Horas trabajadas: ");
//         int horas = scanner.nextInt();

//         System.out.print("Valor por hora: ");
//         double valorPorHora = scanner.nextDouble();

//         // Cálculo del pago total
//         double pagoTotal = horas * valorPorHora;

//         // Impresión del resumen con el formato solicitado
//         System.out.println("-------------------------");
//         System.out.println("RESUMEN DE PAGO");
//         System.out.println("-------------------------");
//         System.out.println("Trabajador: " + nombre);
//         System.out.println("Horas: " + horas);
//         System.out.println("Valor por hora: $" + valorPorHora);
//         System.out.println("Pago total: $" + pagoTotal);

//         scanner.close();
//     }
// }