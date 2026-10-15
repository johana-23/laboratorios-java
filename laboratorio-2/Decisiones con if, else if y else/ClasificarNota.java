/* public class ClasificarNota {

    public static void main(String[] args) {
        double nota = 4.6;

        if (nota >= 3.0) {  System.out.println("Desempeño aprobatorio");
       
        } else if (nota >= 4.0) {
            System.out.println("Desempeño alto");
        } else if (nota >= 4.5) { System.out.println("Desempeño sobresaliente");
          
        } else {
            System.out.println("No aprobado");
        }
    }
} */

/*  ¿Por qué comenzamos por la condición:

nota >= 4.5
y no por:

nota >= 3.0. porque si comenzamos por la condición nota >= 3.0, cualquier nota que sea mayor o igual a 3.0 también será mayor o igual a 4.5, lo que significa que todas las notas mayores o iguales a 4.5 se clasificarán como "Desempeño aprobatorio" en lugar de "Desempeño sobresaliente". Por lo tanto, es importante comenzar con la condición más alta para asegurarnos de que las notas se clasifiquen correctamente según su desempeño. */


public class ClasificarNota {

    public static void main(String[] args) {
       boolean membresiaActiva = false;
        double totalCompra = 250000;

        if (membresiaActiva && totalCompra <= 200000) {
            System.out.println("Aplica beneficio.");
        } else {
            System.out.println("No aplica beneficio.");
        }
    }
}