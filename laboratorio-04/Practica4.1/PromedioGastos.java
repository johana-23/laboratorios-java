public class PromedioGastos {

    public static double totalGastos(double[] gastos){
        double total = 0;
        for(int i = 0; i < gastos.length; i++){
            total += gastos[i];
        }
        return total;
    }

    public static int cantidadSuperior(double[] gastos){
        int cantidad = 0;
        for(int i = 0; i < gastos.length; i++){
            if(gastos[i] > 25000){
                cantidad++;
            }
        }
        return cantidad;
    }

    public static void main(String[] args){
        double[] gastos = {30000, 45000, 20000, 10000};

        System.out.println("Suma de los gastos: " + "$"+ totalGastos(gastos));
        System.out.println("Cantidad de registros: " + gastos.length);
        System.out.println("Promedio: " + "$" +(totalGastos(gastos) / gastos.length));
        System.out.println("Superiores a $25,000: " + cantidadSuperior(gastos));
    }
}
