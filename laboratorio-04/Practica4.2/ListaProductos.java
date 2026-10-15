import java.util.ArrayList;
import java.util.List;

public class ListaProductos {

    public static void main(String[] args) {
        List<String> productos = new ArrayList<>();

             productos.add("Teclado");
             productos.add("Mouse");
             productos.add("Monitor");

             System.out.println(productos);
             System.out.println("Cantidad: " + productos.size());
        System.out.println("=====================");

        productos.set(1, "Mouse inalámbrico");

        System.out.println("Primero: " + productos.get(0));
        System.out.println("¿Hay monitor? " + productos.contains("Monitor"));

        productos.remove("Monitor");

        System.out.println("Después: " + productos);
        
        System.out.println("====================== for");
            for (int i = 0; i < productos.size(); i++) {
             System.out.println((i + 1) + ". " + productos.get(i));
            }   
         
        System.out.println("====================== for each");
            for (String producto : productos) {
             System.out.println(producto);
            }

        System.out.println("======================");

        List<String> nombres = new ArrayList<>();

            nombres.add("Ana");
            nombres.add("Luis");
            nombres.add("Ana");
            nombres.remove("Ana");

            System.out.println(nombres);

            /* ¿Desaparecen las dos apariciones de Ana? Escribe tu predicción y comprueba. si desaparecen. cuando hice el test  solo desaparece la primera aparición de Ana */





    }
}
