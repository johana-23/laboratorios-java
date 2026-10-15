public class RegistroVentas {
    public static void main(String[] args) {
        double[] ventas = {120000, 85000, 95000, 150000, 60000};
        ventas[2] = 100000;

        System.out.println("Primera Venta: " + ventas[0]);
        System.out.println("Última Venta: " + ventas[4]);
        System.out.println("Cantidad de ventas: " + ventas.length);
        System.out.println("Tercera Venta: " + ventas[2]);
    }
}