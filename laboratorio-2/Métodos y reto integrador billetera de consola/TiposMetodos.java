import java.text.DecimalFormat;

public class TiposMetodos {

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.##");

        
        mostrarTitulo("SISTEMA DE FACTURACIÓN");
        mostrarMenu();

        
        double subtotal = calcularSubtotal(2, 50000);
        double descuento = calcularDescuento(subtotal, 0.10);
        double total = subtotal - descuento;

        System.out.println("\nSubtotal: $" + df.format(subtotal));
        System.out.println("Descuento: $" + df.format(descuento));
        System.out.println("Total a pagar: $" + df.format(total));

        
        mostrarDespedida();
    }

    
    static void mostrarTitulo(String titulo) {
        System.out.println("================================");
        System.out.println("  " + titulo);
        System.out.println("================================");
    }

    
    static double calcularSubtotal(int cantidad, double precioUnitario) {
        return cantidad * precioUnitario;
    }

    
    static void mostrarMenu() {
        System.out.println("1. Procesar compra");
        System.out.println("0. Salir");
    }

    
    static double calcularDescuento(double subtotal, double porcentaje) {
        return subtotal * porcentaje;
    }

    
    static void mostrarDespedida() {
        System.out.println("\n¡Gracias por usar el sistema! Hasta pronto.");
    }
}