import java.util.List;


public class PruebaBilleteras {

    public static void main(String[] args) {
        Billetera laura = new Billetera("Laura");
        Billetera carlos = new Billetera("Carlos");

        laura.registrarIngreso(500000, "Salario");
        laura.registrarGasto(80000, "Alimentación");
        carlos.registrarIngreso(200000, "Otros");

        System.out.println("Laura  -> saldo: $" + laura.getSaldo() + ", movimientos: " + laura.getMovimientos().size());
        System.out.println("Carlos -> saldo: $" + carlos.getSaldo() + ", movimientos: " + carlos.getMovimientos().size());
        System.out.println("Historial de Carlos:");
        for (Movimiento m : carlos.getMovimientos()) {
            System.out.println("  " + m);
        }

        System.out.println();
        System.out.println("--- Prueba de copia del historial ---");
        List<Movimiento> copia = laura.getMovimientos();
        copia.clear();
        System.out.println("Copia: " + copia.size());
        System.out.println("Original: " + laura.getMovimientos().size());
    }
}
