import java.util.HashMap;
import java.util.Map;


public class PrediceMapa {

    public static void main(String[] args) {
        Map<String, Double> valores = new HashMap<>();

        valores.put("Salud", 10000.0);
        valores.put("Salud", 20000.0);
        valores.put("Transporte", 5000.0);

        System.out.println(valores.get("Salud"));
        System.out.println(valores.size());
    }
        /*
        Antes de ejecutar — Predice.
         Predicción: Salud -> 20000.0 y tamaño 2.
         Explicación: put() con una clave que ya existe REEMPLAZA el valor anterior.
        Un mapa guarda un solo valor por clave, por eso 10000.0 se pierde y las claves
         distintas (Salud, Transporte) dan tamaño 2.
        */
}
