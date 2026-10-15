import java.text.DecimalFormat;

public class CalculoDescuento {

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.##");

        double subtotal = 200000;
        double descuento = calcularDescuento(subtotal, 0.10);

        System.out.println("Descuento directo: " + descuento);
        System.out.println("Descuento formateado: $" + df.format(descuento));
    }

    
    static double calcularDescuento(double subtotal, double porcentaje) {
        return subtotal * porcentaje;
    }
}
