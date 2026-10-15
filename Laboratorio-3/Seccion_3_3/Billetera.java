// // // public class Billetera {

// // //     private String titular;
// // //     private double saldo;

// // //     public Billetera(String titular) {
// // //         this.titular = titular;
// // //         this.saldo = 0;
// // //     }

// // //     public String getTitular() {
// // //         return titular;
// // //     }

// // //     public double getSaldo() {
// // //         return saldo;
// // //     }

// // //     public boolean registrarIngreso(double valor) {
// // //         if (valor <= 0) {
// // //             return false;
// // //         }

// // //         saldo += valor;
// // //         return true;
// // //     }
// // // }



// // public class Billetera {

// //     private String titular;
// //     private double saldo;

// //     public Billetera(String titular) {
// //         this.titular = titular;
// //         this.saldo = 0;
// //     }

// //     public String getTitular() {
// //         return titular;
// //     }

// //     public double getSaldo() {
// //         return saldo;
// //     }

// //     public boolean registrarIngreso(double valor) {
// //         if (valor <= 0) {
// //             return false;
// //         }

// //         saldo += valor;
// //         return true;
// //     }

// //     public boolean registrarGasto(double valor) {
// //         if (valor <= 0 || valor > saldo) {
// //             return false;
// //         }

// //         saldo -= valor;
// //         return true;
// //     }
// // }




// public class Billetera {

//     private String titular;
//     private double saldo;
//     private int cantidadIngresos;
//     private int cantidadGastos;

//     public Billetera(String titular) {
//         this.titular = titular;
//         this.saldo = 0;
//         this.cantidadIngresos = 0;
//         this.cantidadGastos = 0;
//     }

//     public String getTitular() {
//         return titular;
//     }

//     public double getSaldo() {
//         return saldo;
//     }

//     // Métodos públicos de consulta para los contadores
//     public int getCantidadIngresos() {
//         return cantidadIngresos;
//     }

//     public int getCantidadGastos() {
//         return cantidadGastos;
//     }

//     public boolean registrarIngreso(double valor) {
//         if (valor <= 0) {
//             return false;
//         }

//         saldo += valor;
//         cantidadIngresos++; // Solo se incrementa si la operación fue exitosa
//         return true;
//     }

//     public boolean registrarGasto(double valor) {
//         if (valor <= 0 || valor > saldo) {
//             return false;
//         }

//         saldo -= valor;
//         cantidadGastos++; // Solo se incrementa si la operación fue exitosa
//         return true;
//     }
// }












