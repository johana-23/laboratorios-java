import java.util.List;
import java.util.Map;


public class PruebaBilletera {

    private static int fallos = 0;

    private static void verificar(String nombre, boolean condicion) {
        System.out.println((condicion ? "OK     " : "FALLÓ  ") + nombre);
        if (!condicion) {
            fallos++;
        }
    }

    public static void main(String[] args) {
        // --- Pruebas obligatorias ---
        Billetera b = new Billetera("Laura");
        verificar("Billetera nueva: saldo 0 e historial vacío",
                b.getSaldo() == 0 && b.getMovimientos().isEmpty());

        verificar("Ingreso 200000 aceptado", b.registrarIngreso(200000, "Salario"));
        verificar("Saldo 200000 y 1 movimiento", b.getSaldo() == 200000 && b.getMovimientos().size() == 1);

        verificar("Gasto 50000 aceptado", b.registrarGasto(50000, "Transporte"));
        verificar("Saldo 150000 y 2 movimientos", b.getSaldo() == 150000 && b.getMovimientos().size() == 2);

        verificar("Gasto 300000 rechazado (saldo insuficiente)", !b.registrarGasto(300000, "Salud"));
        verificar("Ingreso 0 rechazado", !b.registrarIngreso(0, "Otros"));
        verificar("Gasto negativo rechazado", !b.registrarGasto(-10, "Otros"));
        verificar("Categoría vacía rechazada (ingreso)", !b.registrarIngreso(1000, ""));
        verificar("Categoría con espacios rechazada (gasto)", !b.registrarGasto(1000, "   "));
        verificar("NaN rechazado", !b.registrarIngreso(Double.NaN, "Otros"));
        verificar("Infinity rechazado", !b.registrarIngreso(Double.POSITIVE_INFINITY, "Otros"));
        verificar("Estado intacto tras rechazos",
                b.getSaldo() == 150000 && b.getMovimientos().size() == 2
                        && b.getTotalIngresos() == 200000 && b.getTotalGastos() == 50000
                        && b.getCantidadIngresos() == 1 && b.getCantidadGastos() == 1);

        // --- Prueba numérica del informe ---
        Billetera r = new Billetera("Laura");
        r.registrarIngreso(1500000, "Salario");
        r.registrarGasto(30000, "Transporte");
        r.registrarGasto(45000, "Alimentación");
        r.registrarGasto(20000, "Transporte");
        r.registrarIngreso(100000, "Otros");
        r.registrarGasto(40000, "Salud");

        System.out.println();
        System.out.println("Cantidad de movimientos: " + r.getMovimientos().size());
        System.out.println("Ingresos realizados: " + r.getCantidadIngresos());
        System.out.println("Gastos realizados: " + r.getCantidadGastos());
        System.out.println("Total ingresado: " + r.getTotalIngresos());
        System.out.println("Total gastado: " + r.getTotalGastos());
        System.out.println("Saldo: " + r.getSaldo());
        System.out.println("Categorías utilizadas: " + r.categoriasUtilizadas().size());
        System.out.println("Gastos por categoría:");
        Map<String, Double> gastos = r.gastosPorCategoria();
        double suma = 0;
        for (Map.Entry<String, Double> e : gastos.entrySet()) {
            System.out.println("  " + e.getKey() + ": " + e.getValue());
            suma += e.getValue();
        }
        System.out.println();

        verificar("6 movimientos", r.getMovimientos().size() == 6);
        verificar("2 ingresos y 4 gastos", r.getCantidadIngresos() == 2 && r.getCantidadGastos() == 4);
        verificar("Total ingresado 1600000", r.getTotalIngresos() == 1600000.0);
        verificar("Total gastado 135000", r.getTotalGastos() == 135000.0);
        verificar("Saldo 1465000", r.getSaldo() == 1465000.0);
        verificar("5 categorías utilizadas", r.categoriasUtilizadas().size() == 5);
        verificar("3 categorías de gasto", r.categoriasDeGastos().size() == 3);
        verificar("Transporte = 50000", gastos.get("Transporte") == 50000.0);
        verificar("Alimentación = 45000", gastos.get("Alimentación") == 45000.0);
        verificar("Salud = 40000", gastos.get("Salud") == 40000.0);
        verificar("Mapa de gastos no incluye ingresos",
                !gastos.containsKey("Salario") && !gastos.containsKey("Otros"));
        verificar("Suma del mapa = total gastado", suma == r.getTotalGastos());
        verificar("Ingresado - gastado = saldo",
                r.getTotalIngresos() - r.getTotalGastos() == r.getSaldo());

        List<Movimiento> ingresos = r.buscarPorTipo("INGRESO");
        List<Movimiento> gastosLista = r.buscarPorTipo("GASTO");
        verificar("Filtro INGRESO: solo ingresos", ingresos.size() == 2
                && ingresos.stream().allMatch(m -> m.getTipo().equals("INGRESO")));
        verificar("Filtro GASTO: solo gastos", gastosLista.size() == 4
                && gastosLista.stream().allMatch(m -> m.getTipo().equals("GASTO")));
        verificar("Tipo desconocido: lista vacía", r.buscarPorTipo("OTRO").isEmpty());
        verificar("Historial respeta el orden",
                r.getMovimientos().get(0).getCategoria().equals("Salario")
                        && r.getMovimientos().get(5).getCategoria().equals("Salud"));

        // --- Movimiento inválido ---
        boolean lanzo = false;
        try {
            new Movimiento("TRANSFERENCIA", "X", 10);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        verificar("Movimiento con tipo inválido lanza IllegalArgumentException", lanzo);

        System.out.println();
        System.out.println(fallos == 0 ? "TODAS LAS PRUEBAS PASARON" : "Pruebas fallidas: " + fallos);
    }
}
