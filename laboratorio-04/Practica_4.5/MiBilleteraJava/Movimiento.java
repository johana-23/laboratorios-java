
public class Movimiento {

    private final String tipo;
    private final String categoria;
    private final double valor;

    public Movimiento(String tipo, String categoria, double valor) {
        if (tipo == null || !(tipo.equals("INGRESO") || tipo.equals("GASTO"))) {
            throw new IllegalArgumentException("Tipo inválido: debe ser INGRESO o GASTO.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Categoría obligatoria.");
        }
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser positivo y finito.");
        }
        this.tipo = tipo;
        this.categoria = categoria.trim();
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return tipo + " | " + categoria + " | $" + valor;
    }
}
