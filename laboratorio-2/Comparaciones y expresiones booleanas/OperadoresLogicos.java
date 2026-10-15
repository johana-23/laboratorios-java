 /* // public class OperadoresLogicos 
 //     public static void main(String[] args) {
 //        int edad = 21;
 //         boolean documentoValido = true
 //         boolean puedeIngresar = edad >= 18 && documentoValido
 //         System.out.println(puedeIngresar);
 //     }  
 // }
  */

//  public class OperadoresLogicos {
//      public static void main(String[] args) {
//         boolean tieneCarnet = false;
//         boolean tieneAutorizacion = true;

//         boolean puedeEntrar = tieneCarnet || tieneAutorizacion;

//         System.out.println(puedeEntrar);
//      }  
//  }

public class OperadoresLogicos {
    public static void main(String[] args) {
       int edad = 17;
        boolean acompanado = true;
            
        boolean resultado1 = edad >= 18;
        boolean resultado2 = edad >= 18 || acompanado;
        boolean resultado3 = edad >= 18 && acompanado;
        boolean resultado4 = !acompanado;
            
        System.out.println(resultado1);
        System.out.println(resultado2);
        System.out.println(resultado3);
        System.out.println(resultado4);
            }  
}