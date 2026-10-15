public class FacturaSimple {

    public static void main(String[] args) {
        // 1. Declaración e inicialización de variables requeridas
        String nombreProducto = "Teclado Mecánico";
        double precioUnitario = 100000.0;
        int cantidad = 2;
        double porcentajeDescuento = 0.10; // Equivalente al 10%
        
        // Reto adicional: Porcentaje de impuesto (ejemplo: IVA del 19%)
        final double PORCENTAJE_IMPUESTO = 0.19;

        // 2. Cálculos paso a paso (guardando cada resultado en su propia variable)
        double subtotal = precioUnitario * cantidad;
        double valorDescuento = subtotal * porcentajeDescuento;
        double totalDespuesDescuento = subtotal - valorDescuento; // Base después del descuento

        // Reto adicional: Cálculo del impuesto y total final
        double valorImpuesto = totalDespuesDescuento * PORCENTAJE_IMPUESTO;
        double totalFinal = totalDespuesDescuento + valorImpuesto;

        // 3. Muestra de resultados en pantalla
        System.out.println("Producto: " + nombreProducto);
        System.out.println("----------------------------------------");
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Descuento: $" + valorDescuento);
        System.out.println("Total: $" + totalDespuesDescuento);
        
        System.out.println("----------------------------------------");
        System.out.println("Base después del descuento: $" + totalDespuesDescuento);
        System.out.println("Valor del impuesto (19%): $" + valorImpuesto);
        System.out.println("Total final con impuesto: $" + totalFinal);
    }
}