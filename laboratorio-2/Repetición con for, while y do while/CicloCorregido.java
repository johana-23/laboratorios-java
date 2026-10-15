public class CicloCorregido {

    public static void main(String[] args) {
        int numero = 1;

        while (numero <= 5) {
            System.out.println(numero);
            numero++; // Modificación crucial: incrementa el valor en 1 en cada iteración
        }

        System.out.println("Fin del ciclo.");
    }
}