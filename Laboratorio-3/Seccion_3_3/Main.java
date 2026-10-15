// // // // // public class Main {

// // // // //     public static void main(String[] args) {
// // // // //         Billetera billetera = new Billetera("Laura");

// // // // //         boolean primera = billetera.registrarIngreso(100000);
// // // // //         boolean segunda = billetera.registrarIngreso(-5000);

// // // // //         System.out.println("Primer ingreso: " + primera);
// // // // //         System.out.println("Segundo ingreso: " + segunda);
// // // // //         System.out.println("Saldo: $" + billetera.getSaldo());
// // // // //     }
// // // // // }






// // // // public class Main {

// // // //     public static void main(String[] args) {
// // // //         Billetera billetera = new Billetera("Laura");

// // // //         // Ingresar $100.000
// // // //         billetera.registrarIngreso(100000);

// // // //         // Gastar $30.000 (Aceptado)
// // // //         boolean primerGasto = billetera.registrarGasto(30000);
        
// // // //         // Intentar gastar $90.000 (Rechazado, supera el saldo actual)
// // // //         boolean segundoGasto = billetera.registrarGasto(90000);

// // // //         System.out.println("Primer gasto ($30.000): " + primerGasto);
// // // //         System.out.println("Segundo gasto ($90.000): " + segundoGasto);
// // // //         System.out.println("Saldo final: $" + billetera.getSaldo());
// // // //     }
// // // // }

// // // public class Main {

// // //     public static void main(String[] args) {
// // //         CuentaPuntos cuenta = new CuentaPuntos("Cristian");

// // //         System.out.println("Puntos iniciales: " + cuenta.consultarPuntos());

// // //         // Sumar 80 -> aceptado
// // //         boolean suma = cuenta.sumarPuntos(80);
// // //         System.out.println("Sumar: 80        -> " + (suma ? "aceptado" : "rechazado"));

// // //         // Canjear 30 -> aceptado
// // //         boolean canje1 = cuenta.canjearPuntos(30);
// // //         System.out.println("Canjear: 30      -> " + (canje1 ? "aceptado" : "rechazado"));

// // //         // Canjear 100 -> rechazado (supera los puntos disponibles)
// // //         boolean canje2 = cuenta.canjearPuntos(100);
// // //         System.out.println("Canjear: 100     -> " + (canje2 ? "aceptado" : "rechazado"));

// // //         // Puntos finales
// // //         System.out.println("Puntos finales: " + cuenta.consultarPuntos());
// // //     }
// // // }







// // public class Main {

// //     public static void main(String[] args) {
// //         Billetera laura = new Billetera("Laura");
// //         Billetera diego = new Billetera("Diego");

// //         laura.registrarIngreso(150000);
// //         diego.registrarIngreso(70000);

// //         laura.registrarGasto(25000);

// //         System.out.println("Saldo de Laura: $" + laura.getSaldo());
// //         System.out.println("Saldo de Diego: $" + diego.getSaldo());
// //     }
// // }


// public class Main {
//     public static void main(String[] args) {
//         Billetera billetera = new Billetera("Laura");
//         // billetera.saldo = 500000;

//
//         billetera.registrarIngreso(500000);

//         System.out.println("Saldo actual: $" + billetera.getSaldo());
//     }
// }


public class Main {

    public static void main(String[] args) {
        Billetera billetera = new Billetera("Laura");

        // Pruebas de ingresos y gastos
        billetera.registrarIngreso(100000); // Aceptado (ingresos: 1)
        billetera.registrarIngreso(-2000);  // Rechazado (ingresos: 1)
        
        billetera.registrarGasto(30000);    // Aceptado (gastos: 1)
        billetera.registrarGasto(90000);    // Rechazado, supera el saldo (gastos: 1)

        // Resultados en pantalla
        System.out.println("Cantidad de ingresos: " + billetera.getCantidadIngresos());
        System.out.println("Cantidad de gastos: " + billetera.getCantidadGastos());
        System.out.println("Saldo: " + billetera.getSaldo());
    }
}