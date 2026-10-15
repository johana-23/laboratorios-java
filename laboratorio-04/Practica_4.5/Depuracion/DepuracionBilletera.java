import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DepuracionBilletera {

    static double saldo;
    static List<String> historial;

    public static void main(String[] args) {

        // 1. Agregar el movimiento antes de validar el monto.
        //    Problema: si el monto es inválido, el movimiento ya quedó en el historial aunque se rechace.
        historial = new ArrayList<>();
        historial.add("INGRESO -5");                 // ERROR: se agrega antes de validar
        double monto = -5;
        if (monto <= 0) { /* rechazado, pero ya se agregó */ }
        System.out.println("1. Con error, historial: " + historial.size() + " (debería ser 0)");
        historial = new ArrayList<>();
        if (monto > 0) { historial.add("INGRESO " + monto); }   // CORRECTO: validar primero
        System.out.println("1. Corregido, historial: " + historial.size());

        // 2. Aceptar un gasto que supera el saldo.
        //    Problema: falta la condición valor > saldo, y el saldo queda negativo.
        saldo = 100;
        saldo -= 300;                                // ERROR: sin comprobar el saldo
        System.out.println("2. Con error, saldo: " + saldo);
        saldo = 100;
        if (300 <= saldo) { saldo -= 300; }          // CORRECTO
        System.out.println("2. Corregido, saldo: " + saldo);

        // 3. Devolver directamente la lista interna.
        //    Problema: quien la recibe puede modificar el historial sin pasar por las reglas.
        List<String> interna = new ArrayList<>(List.of("m1", "m2"));
        List<String> expuesta = interna;             // ERROR: misma lista
        expuesta.add("movimiento colado desde Main");
        System.out.println("3. Con error, interna: " + interna.size() + " (debería ser 2)");
        interna = new ArrayList<>(List.of("m1", "m2"));
        List<String> copia = new ArrayList<>(interna);   // CORRECTO: copia
        copia.add("no afecta");
        System.out.println("3. Corregido, interna: " + interna.size());

        // 4. Lista static compartida entre billeteras.
        //    Problema: static pertenece a la clase, no al objeto; dos billeteras comparten historial.
        System.out.println("4. Con error: " + Cuenta.compartida.size() + " movimientos compartidos");
        CuentaCorrecta c1 = new CuentaCorrecta();
        CuentaCorrecta c2 = new CuentaCorrecta();
        c1.propia.add("solo c1");
        System.out.println("4. Corregido, c2 tiene: " + c2.propia.size());

        // 5. Agrupar con put(categoria, valor) sin sumar el acumulado.
        //    Problema: put reemplaza; solo queda el último valor.
        Map<String, Double> mal = new HashMap<>();
        mal.put("Transporte", 30000.0);
        mal.put("Transporte", 20000.0);              // ERROR: reemplaza
        System.out.println("5. Con error: " + mal.get("Transporte") + " (debería ser 50000.0)");
        Map<String, Double> bien = new HashMap<>();
        bien.put("Transporte", bien.getOrDefault("Transporte", 0.0) + 30000.0);
        bien.put("Transporte", bien.getOrDefault("Transporte", 0.0) + 20000.0);
        System.out.println("5. Corregido: " + bien.get("Transporte"));

        // 6. Calcular categorías sin considerar que se repiten.
        //    Problema: una lista cuenta cada aparición; un Set cuenta categorías distintas.
        List<String> lista = new ArrayList<>(List.of("Transporte", "Transporte", "Salud"));
        System.out.println("6. Con error (lista): " + lista.size() + " (debería ser 2)");
        Set<String> conjunto = new HashSet<>(lista);
        System.out.println("6. Corregido (Set): " + conjunto.size());

        // 7. parseDouble sin atender texto no numérico.
        //    Problema: "abc" lanza NumberFormatException y el programa se interrumpe.
        try {
            Double.parseDouble("abc");               // ERROR si no hay try/catch
        } catch (NumberFormatException e) {
            System.out.println("7. Corregido: se atendió NumberFormatException y el programa sigue.");
        }
    }

    static class Cuenta {
        static List<String> compartida = new ArrayList<>(List.of("de la cuenta A")); // ERROR: static
    }

    static class CuentaCorrecta {
        List<String> propia = new ArrayList<>();      // CORRECTO: una lista por instancia
    }
    
    /**
        * Sección 21 — Los 7 errores de depuración, cada uno con su versión errónea y su corrección.
        * (Se hacen sobre estructuras mínimas para no dañar el proyecto principal.)
        */
}
