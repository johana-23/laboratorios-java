/* public class CompararCantidades {
    public static void main(String[] args){
        int inventario = 12;
        int cantidadSolicitada = 8;

        boolean hayInventarioSuficiente = inventario >= cantidadSolicitada;
        boolean hayInventarioInsuficiente2 = cantidadSolicitada == inventario;

        System.out.println(hayInventarioSuficiente);
        System.out.println(hayInventarioInsuficiente2);

         int cansoli = 18;
        boolean cansoli1 =   cansoli > inventario;
        System.out.println(cansoli1);
    }
}
  */

public class CompararCantidades {
    public static void main(String[] args){
       String ciudad = "Bogotá";

        System.out.println(ciudad.equals("Bogotá"));
        System.out.println(ciudad.equals("Medellín"));
        System.out.println(ciudad.equalsIgnoreCase("bogotá"));
    }
}