import java.util.HashMap;
import java.util.Map;


public class DepuracionClave {

    public static void main(String[] args) {
        Map<String, Double> totales = new HashMap<>();

        try {
            double salud = totales.get("Salud"); // falla: get devuelve null
            System.out.println(salud);
        } catch (NullPointerException e) {
            System.out.println("Error: get() devolvió null y no puede convertirse a double.");
        }

        // Corrección
        double salud = totales.getOrDefault("Salud", 0.0);
        System.out.println("Salud (corregido): " + salud);
    }
    /**
    Depuración — Clave inexistente.
    get() devuelve null cuando la clave no existe. Asignar ese null a un double primitivo
    obliga a "desempaquetar" el Double y provoca NullPointerException en ejecución.
    */
}
