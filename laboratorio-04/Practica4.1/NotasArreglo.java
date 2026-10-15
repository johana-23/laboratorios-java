import java.util.Scanner;
public class NotasArreglo {

    static double Promedio(double[] notas){
        double suma = 0;
        for(double nota : notas) {
            suma += nota;
        }
        return suma / notas.length;
    }

    static double NMayor(double[] notas){
        double mayor = notas[0];
        for(double nota : notas){
            if(nota > mayor){
                mayor = nota;
            }
        }
        return mayor;
    }

    static double NMenor(double[] notas){
        double menor = notas[0];
        for(double nota : notas) {
            if(nota < menor) {
                menor = nota;
            }
        }
        return menor;
    }

    static int notasAprobadas(double[] notas){
        int cantidad = 0;
        for(double nota : notas) {
            if(nota >= 3.0) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la cantidad de notas a evaluar: ");
        int cantidadNotas = scanner.nextInt();

        double[] notas = new double[cantidadNotas];
        
        for(int i = 0; i < cantidadNotas; i++) {
            System.out.print("Ingrese la nota " + (i + 1) + ": ");
            notas[i] = scanner.nextDouble();
        }



    System.out.println("---------------------------------");
        System.out.println("Notas ingresadas:");
        for(int i = 0; i < cantidadNotas; i++) {
            System.out.println("Nota " + (i + 1) + ": " + notas[i]);
        }
    System.out.println("---------------------------------");
        System.out.println(" ■ Promedio de notas: " + Promedio(notas));
        System.out.println(" ■ Nota Mayor: " + NMayor(notas));
        System.out.println(" ■ Nota Menor: " + NMenor(notas));
        System.out.println(" ■ Cantidad de notas aprobadas: " + notasAprobadas(notas));

    scanner.close();
    }
}
