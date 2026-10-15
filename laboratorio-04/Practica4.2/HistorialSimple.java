import java.util.ArrayList;
import java.util.List;

public class HistorialSimple {

    public static void main(String[] args) {
        List<Movimiento> movimientos = new ArrayList<>();

        movimientos.add(new Movimiento("INGRESO", "Salario", 1500000));
        movimientos.add(new Movimiento("GASTO", "Transporte", 30000));
        movimientos.add(new Movimiento("GASTO", "Alimentación", 45000));
        movimientos.add(new Movimiento("GASTO", "Entretenimiento", 20000));

        

        for (Movimiento movimiento : movimientos) {
            System.out.println(
                movimiento.getTipo() + " | "
                + movimiento.getCategoria() + " | $"
                + movimiento.getValor()
            );
        }
        System.out.println("Movimientos: " + movimientos.size());

        double totalGasto = 0;

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals("GASTO")) {
                totalGasto += movimiento.getValor();
            }
        }

        System.out.println("Total de gastos: $" + totalGasto);


        System.out.println("==================");
        System.out.println("copiando la lista de movimientos");
        

        List<Movimiento> copiaMovimientos = new ArrayList<>(movimientos);
        System.out.println("eliminacion: " + copiaMovimientos.remove(0));

        System.out.println("Movimientos lista original: " + movimientos.size());
        System.out.println("Movimientos lista copia: " + copiaMovimientos.size());

      /*   List<String> nombres = new ArrayList<>();
        nombres.add("Laura");
        nombres.add("Juan");
 */
        /* ¿Qué ventaja aporta declarar el tipo de los elementos?
         la ventaja es que al declarar el tipo de los elementos, se puede garantizar que la lista solo contendrá elementos de ese tipo específico, lo que ayuda a prevenir errores de tipo en tiempo de compilación y facilita la lectura y el mantenimiento del código. Además, permite aprovechar las características de tipado fuerte del lenguaje, como la autocompletación y la verificación de tipos en tiempo de compilación.
         */

    }
}