import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Billetera {

    private final String titular;
    private double saldo;
    private int cantidadIngresos;
    private double totalIngresos;
    private int cantidadGastos;
    private double totalGastos;
    
    private final List<Movimiento> movimientos;

    public Billetera(String titular) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("Titular obligatorio.");
        }
        this.titular = titular.trim();
        this.saldo = 0;
        this.cantidadIngresos = 0;
        this.totalIngresos = 0;
        this.cantidadGastos = 0;
        this.totalGastos = 0;
        this.movimientos = new ArrayList<>();
    }

    // ---------- Operaciones ----------

    public boolean registrarIngreso(double valor, String categoria) {
        
        if (!Double.isFinite(valor) || valor <= 0) {
            return false;
        }
        if (categoria == null || categoria.isBlank()) {
            return false;
        }
        // 2. Aplicar cambios
        Movimiento movimiento = new Movimiento("INGRESO", categoria, valor);
        saldo += valor;
        cantidadIngresos++;
        totalIngresos += valor;
        movimientos.add(movimiento);
        return true;
    }

    public boolean registrarGasto(double valor, String categoria) {
        if (!Double.isFinite(valor) || valor <= 0) {
            return false;
        }
        if (categoria == null || categoria.isBlank()) {
            return false;
        }
        if (valor > saldo) {
            return false;
        }
        Movimiento movimiento = new Movimiento("GASTO", categoria, valor);
        saldo -= valor;
        cantidadGastos++;
        totalGastos += valor;
        movimientos.add(movimiento);
        return true;
    }

    // ---------- Consultas ----------

   
    public List<Movimiento> getMovimientos() {
        return new ArrayList<>(movimientos);
    }

   
    public List<Movimiento> buscarPorTipo(String tipo) {
        List<Movimiento> encontrados = new ArrayList<>();
        if (tipo == null) {
            return encontrados;
        }
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals(tipo)) {
                encontrados.add(movimiento);
            }
        }
        return encontrados;
    }

    
    public Set<String> categoriasUtilizadas() {
        Set<String> categorias = new HashSet<>();
        for (Movimiento movimiento : movimientos) {
            categorias.add(movimiento.getCategoria());
        }
        return categorias;
    }


    public Set<String> categoriasDeGastos() {
        Set<String> categorias = new HashSet<>();
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals("GASTO")) {
                categorias.add(movimiento.getCategoria());
            }
        }
        return categorias;
    }

  
    public Map<String, Double> gastosPorCategoria() {
        Map<String, Double> totales = new HashMap<>();
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals("GASTO")) {
                String categoria = movimiento.getCategoria();
                double acumulado = totales.getOrDefault(categoria, 0.0);
                totales.put(categoria, acumulado + movimiento.getValor());
            }
        }
        return totales;
    }

  

    public String getTitular() { return titular; }
    public double getSaldo() { return saldo; }
    public int getCantidadIngresos() { return cantidadIngresos; }
    public double getTotalIngresos() { return totalIngresos; }
    public int getCantidadGastos() { return cantidadGastos; }
    public double getTotalGastos() { return totalGastos; }
}
