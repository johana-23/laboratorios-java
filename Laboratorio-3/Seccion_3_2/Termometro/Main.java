package Termometro;

public class Main {
    public static void main(String[] args){
        Termometro temp_1 = new Termometro(0);
        Termometro temp_2 = new Termometro(25);

        System.out.println("==============================");
        System.out.println("      Conversión °C a °F      ");
        System.out.println("==============================");
        temp_1.mostrarCelsius();
        temp_2.mostrarCelsius();
        System.out.println("==============================");

    }
}
