package Productos;
public class Main{
    public static void main(String[] args){
        Producto producto = new Producto();

        producto.nombre = "Teclado";
        producto.precio = 79900;
        producto.cantidad = 3;

    /* ------ Segundo Producto ------ */
        Producto segundo = new Producto();

        segundo.nombre = "Mouse";
        segundo.precio = 51000;
        segundo.cantidad = 7;

    /* ------ Tercer Producto ------ */
        Producto tercero = new Producto();

        tercero.nombre = "Monitor";
        tercero.precio = 599900;
        tercero.cantidad = 12;

    /* Total */
    double total = producto.calcularSubtotal() + segundo.calcularSubtotal() + tercero.calcularSubtotal();

/* ========== Recibo de Compra ========== */
        System.out.println("=========================");
        System.out.println("          Recibo         ");
        System.out.println("=========================");
        producto.mostrarDetalle();
        System.out.println("=========================");
        segundo.mostrarDetalle();
        System.out.println("=========================");
        tercero.mostrarDetalle();
        System.out.println("=========================");
        System.out.println("Total: $" + total);
        System.out.println("=========================");

    }  
}

// ¿Cuántas clases definimos?
// Una. La clase de Producto.

// ¿Cuántos objetos creamos?
// Creamos dos objetos: Teclado y Mouse. Mediante el uso de la misma clase: Producto.

// Si cambiamos segundo.cantidad, ¿cambiará también producto.cantidad?
// No cambia, ya que segundo.cantidad tiene su valor definido en su propio atributo.

// ¿Qué ocurre cuando intentas llamar un método sobre una referencia null?
/* La terminal mostrará una excepción en la que no se ejecutará el método nostrarDetalle(). 
   Porque no tiene un objeto que mostrar. 
*/

 /* ¿Qué ocurre cuando intentas llamar un método sobre una referencia null?
Cuando intentas llamar un método sobre una referencia null en Java, se produce una excepción llamada `NullPointerException`. Esto ocurre porque estás intentando acceder a un método o propiedad de un objeto que no ha sido inicializado (es decir, que no apunta a ninguna instancia de un objeto). */

/* asegúrese de poder explicar qué es una clase, qué es un objeto, qué hace newy por qué los atributos de dos objetos Productono tienen por qué guardar los mismos valores.
Una clase es una plantilla o un modelo que define las características y comportamientos de un tipo de objeto. En otras palabras, una clase describe qué atributos (propiedades) y métodos (funciones) tendrá un objeto de ese tipo. Por ejemplo, en el código proporcionado, la clase `Producto` define los atributos `nombre`, `precio` y `cantidad`, así como los métodos `calcularSubtotal()` y `mostrarDetalle()`.  el new crea un nuevo objeto de la clase Producto. por que cada objeto tiene su propia instancia con valores independientes. */