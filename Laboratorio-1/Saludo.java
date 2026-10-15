// import java.util.Scanner;

// public class Saludo {

//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in);

//         System.out.print("Escribe tu nombre: ");
//         String nombre = scanner.nextLine();

//         System.out.println("Hola, " + nombre);

//         scanner.close();
//     }
// }








import java.util.Scanner;

public class Saludo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Edad: ");
int edad = scanner.nextInt();

System.out.print("Cantidad: ");
int cantidad = scanner.nextInt();

System.out.print("Precio: ");
double Precio = scanner.nextDouble();
scanner.close();
    }
}