import java.text.DecimalFormat;

public class MostrarProductos {

    public static void main(String[] args) {
        // Llamadas al método con diferentes productos y precios
        mostrarProducto("Teclado Mecánico", 45050);
        mostrarProducto("Ratón Inalámbrico", 25000);
        mostrarProducto("Monitor 24 pulgadas", 180099);
    }

    // Método que recibe dos parámetros: nombre (String) y precio (double)
    static void mostrarProducto(String nombre, double precio) {
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Producto: " + nombre + " | Precio: $" + df.format(precio));
    }
}