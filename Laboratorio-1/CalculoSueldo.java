// public class CalculoSueldo {

//     public static void main(String[] args) {
//         final double APORTE = 0.04; 
//         String nombreTrabajador = "Carlos Pérez";
//         int horasTrabajadas = 40;
//         double valorPorHora = 15000.50;

       
//         double sueldoBruto = horasTrabajadas * valorPorHora;
//         double valorDescuento = sueldoBruto * APORTE; 
//         double sueldoNeto = sueldoBruto - valorDescuento;

//         // 3. Resultados
//         System.out.println("Trabajador: " + nombreTrabajador);
//         System.out.println("Sueldo Bruto: $" + sueldoBruto);
//         System.out.println("Descuento de Aporte (4%): $" + valorDescuento);
//         System.out.println("Sueldo Neto a pagar: $" + sueldoNeto);
//     }
// }














public class CalculoSueldo {

    public static void main(String[] args) {
        int unidades = 4;
double precio = 25000.0;
double descuento = 0.20;

double subtotal = unidades * precio;
double valorDescuento = subtotal * descuento;
double total = subtotal - valorDescuento;

System.out.println(total);}}





