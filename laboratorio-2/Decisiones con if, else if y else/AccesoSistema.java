/* import java.util.Scanner;

public class AccesoSistema {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String usuarioCorrecto = "admin";
        String claveCorrecta = "java21";
        System.out.print("Ingrese su nombre de usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Ingrese su clave: ");
        String clave = scanner.nextLine();

        if(usuario.equals(usuarioCorrecto) && clave.equals(claveCorrecta)) {
            System.out.println("Acceso concedido.");
        } else {
            System.out.println("Acceso denegado.");

        }
    }
}
 */

public class AccesoSistema {
    public static void main(String[] args) {
     int edad = 20;
        boolean tieneDocumento = true;

        if (edad >= 18 && tieneDocumento) {
            if (tieneDocumento) {
                System.out.println("Ingreso autorizado.");
            } else {
                System.out.println("Debe presentar documento.");
            }
        } else {
            System.out.println("No cumple la edad requerida.");
            }
    }
}