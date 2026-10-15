public class CalculoCompra {


    public static void main(String[] args) {
        double precio = 150000.0;
        int cantidad = 9;
        double porcentajeDescuento = 0.75;

        double subtotal = precio * cantidad;
        double descuento = subtotal * porcentajeDescuento;
        double total = subtotal - descuento;

        System.out.println("Precio unitario: $" + precio);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Descuento: $" + descuento);
        System.out.println("Total: $" + total);
    }
}

