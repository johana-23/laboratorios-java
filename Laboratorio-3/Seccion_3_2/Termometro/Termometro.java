package Termometro;
public class Termometro{

    double Temp_Celsius;

    public Termometro(double Temp_Celsius){
        this.Temp_Celsius = Temp_Celsius;
    }


/* ========== Métodos ========== */
    double conversionFahrenheit(){
        return (Temp_Celsius * 9 / 5) + 32;
    }

    void mostrarCelsius(){
        System.out.println(" ■ " + Temp_Celsius + "°C   -->   " + conversionFahrenheit() + "°F");
    }
}