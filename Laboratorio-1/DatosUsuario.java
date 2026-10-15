import java.util.Scanner;

public class DatosUsuario {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        System.out.print("Ingrese su nombre completo: ");
        String nombre = scanner.nextLine(); // Permite leer nombres completos con espacios

        System.out.print("Ingrese su ciudad de residencia: ");
        String ciudad = scanner.nextLine();

        System.out.print("Ingrese su edad: ");
        int edad = scanner.nextInt();

        System.out.print("Ingrese la cantidad de cursos terminados: ");
        int cursosTerminados = scanner.nextInt();

       
        System.out.println("\n========================================");
        System.out.println("          FICHA DEL USUARIO             ");
        System.out.println("========================================");
        System.out.println("Nombre:            " + nombre);
        System.out.println("Ciudad:            " + ciudad);
        System.out.println("Edad:              " + edad + " años");
        System.out.println("Cursos terminados: " + cursosTerminados);
        System.out.println("========================================");

        scanner.close();
    }
}