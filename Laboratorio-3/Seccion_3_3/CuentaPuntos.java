// public class CuentaPuntos {

//     private String titular;
//     private int puntos;

//     // Constructor que recibe el titular e inicia puntos en cero
//     public CuentaPuntos(String titular) {
//         this.titular = titular;
//         this.puntos = 0;
//     }

//     // Método para consultar los puntos actuales
//     public int consultarPuntos() {
//         return puntos;
//     }

//     // Método para sumar puntos (rechaza cantidades no positivas)
//     public boolean sumarPuntos(int cantidad) {
//         if (cantidad <= 0) {
//             return false;
//         }

//         puntos += cantidad;
//         return true;
//     }

//     // Método para canjear puntos (rechaza cantidades no positivas o superiores al total)
//     public boolean canjearPuntos(int cantidad) {
//         if (cantidad <= 0 || cantidad > puntos) {
//             return false;
//         }

//         puntos -= cantidad;
//         return true;
//     }
// }
















