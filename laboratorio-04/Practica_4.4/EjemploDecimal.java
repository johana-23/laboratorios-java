
public class EjemploDecimal {

    public static void main(String[] args) {
        String entrada = "25000.50";
        double precio = Double.parseDouble(entrada);
        System.out.println(precio);

        
        String[] pruebas = {"25000.50", "NaN", "Infinity", "-5", "0"};
        for (String texto : pruebas) {
            double valor = Double.parseDouble(texto);
            if (!Double.isFinite(valor) || valor <= 0) {
                System.out.println(texto + " -> Debes escribir un monto positivo y finito.");
            } else {
                System.out.println(texto + " -> válido");
            }
        }
    }
}
