// // // // import java.util.Scanner;

// // // // public class Main {

// // // //     public static void main(String[] args) {
// // // //         Scanner scanner = new Scanner(System.in);

// // // //         System.out.print("Titular: ");
// // // //         String nombre = scanner.nextLine();

// // // //         CuentaPuntos cuenta = new CuentaPuntos(nombre);

// // // //         System.out.print("Puntos a registrar: ");
// // // //         int cantidad = scanner.nextInt();

// // // //         boolean aceptado = cuenta.sumarPuntos(cantidad);

// // // //         if (aceptado) {
// // // //             System.out.println("Operación registrada.");
// // // //         } else {
// // // //             System.out.println("La cantidad debe ser positiva.");
// // // //         }

// // // //         System.out.println("Titular: " + cuenta.getTitular());
// // // //         System.out.println("Puntos: " + cuenta.getPuntos());

// // // //         scanner.close();
// // // //     }
// // // // }










// // // import java.util.Scanner;

// // // public class Main {

// // //     public static void main(String[] args) {
// // //         Scanner scanner = new Scanner(System.in);

// // //         System.out.print("Titular de la cuenta: ");
// // //         String nombre = scanner.nextLine();

// // //         CuentaPuntos cuenta = new CuentaPuntos(nombre);

        
// // //         System.out.println("Puntos iniciales: " + cuenta.getPuntos());

        
// // //         System.out.print("Ingrese la primera cantidad de puntos: ");
// // //         int cantidad1 = scanner.nextInt();
// // //         boolean aceptado1 = cuenta.sumarPuntos(cantidad1);
        
// // //         if (aceptado1) {
// // //             System.out.println("-> Primera cantidad registrada exitosamente.");
// // //         } else {
// // //             System.out.println("-> Error: La cantidad debe ser positiva.");
// // //         }

        
// // //         System.out.print("Ingrese la segunda cantidad de puntos: ");
// // //         int cantidad2 = scanner.nextInt();
// // //         boolean aceptado2 = cuenta.sumarPuntos(cantidad2);
        
// // //         if (aceptado2) {
// // //             System.out.println("-> Segunda cantidad registrada exitosamente.");
// // //         } else {
// // //             System.out.println("-> Error: La cantidad debe ser positiva.");
// // //         }

        
// // //         System.out.println("\n--- Resumen Final ---");
// // //         System.out.println("Titular: " + cuenta.getTitular());
// // //         System.out.println("Puntos finales: " + cuenta.getPuntos());

// // //         scanner.close();
// // //     }
// // // }





// // public class Main {
// //     public static void main(String[] args) {
// //         CuentaPuntos cuenta = new CuentaPuntos("Laura");

// //         // INCORRECTO: Esto genera error de compilación porque el método no existe
// //         // cuenta.agregarPuntos(50);

// //         // CORRECTO: Usamos el nombre exacto definido en la clase CuentaPuntos
// //         boolean aceptado = cuenta.sumarPuntos(50);

// //         if (aceptado) {
// //             System.out.println("Puntos sumados correctamente. Total: " + cuenta.getPuntos());
// //         }
// //     }
// // }















// import java.util.Scanner;

// public class Main {

//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in);

//         System.out.print("Ingrese el primer número: ");
//         double num1 = scanner.nextDouble();

//         System.out.print("Ingrese el segundo número: ");
//         double num2 = scanner.nextDouble();

//         Calculadora calc = new Calculadora();

//         double resultadoSuma = calc.sumar(num1, num2);
//         double resultadoResta = calc.restar(num1, num2);
//         double resultadoMultiplicacion = calc.multiplicar(num1, num2);

//         System.out.println("Suma: " + resultadoSuma);
//         System.out.println("Resta: " + resultadoResta);
//         System.out.println("Multiplicación: " + resultadoMultiplicacion);

//         scanner.close();
//     }
// }
