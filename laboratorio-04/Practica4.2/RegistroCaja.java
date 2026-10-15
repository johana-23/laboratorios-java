import java.util.ArrayList;
import java.util.List;

public class RegistroCaja {
    public static void main(String[] args) {
        List<Movimiento> movimientos = new ArrayList<>();
    
        movimientos.add(new Movimiento("INGRESO", "Salario", 2000000));
        movimientos.add(new Movimiento("GASTO", "salon de belleza", 30000));
        movimientos.add(new Movimiento("INGRESO", "venta", 450000));  
        movimientos.add(new Movimiento("GASTO", "Entretenimiento", 20000));
    
        for(int i = 0; i < movimientos.size(); i++) {
            Movimiento movimiento = movimientos.get(i);
            System.out.println((i + 1) + ". " + movimiento.getTipo() + " | " + movimiento.getCategoria() + " | $" + movimiento.getValor());
        }

        double totalIngresos = 0;
        double totalGastos = 0;

        for(Movimiento movimiento : movimientos){
            if(movimiento.getTipo().equals("INGRESO")){ totalIngresos += movimiento.getValor(); }
            else if(movimiento.getTipo().equals("GASTO")){ totalGastos += movimiento.getValor(); }
        }
    
        System.out.println("Total de ingresos: $" + totalIngresos);
        System.out.println("Total de gastos: $" + totalGastos);
        System.out.println("cantidad de movimientos: " + movimientos.size());

        /*  cuándo prefieres un arreglo y cuándo una lista, qué representa List<Movimiento>y por qué no conviene devolver directamente la lista interna de una billetera.

            un arreglo lo utilizaria  cuando  quiero tener un tamaño fijo y una lista cuando necesito flexibilidad para aumentar o disminuir su cantidad de elementos durante la ejecución. List<Movimiento> representa una lista de objetos de tipo Movimiento, lo que permite almacenar y manipular múltiples movimientos de manera organizada. No conviene devolver directamente la lista interna de una billetera porque esto podría exponer la estructura interna y permitir modificaciones no controladas desde fuera de la clase, lo que podría comprometer la integridad de los datos. En su lugar, es mejor proporcionar métodos para acceder a los datos de manera segura y controlada.
            
          */
    
    
    }
}
