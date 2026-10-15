package RegistroEstudiantes;

public class Estudiante {

    String nombre;
    String ficha;
    int trimestre;
    double promedio;

    //Objeto
    public Estudiante(String nombre, String ficha, int trimestre, double promedio){
        this.nombre = nombre;
        this.ficha = ficha;
        this.trimestre = trimestre;
        this.promedio = promedio;
    }
    
        boolean estado = this.promedio > 3.0;
        
    void mostrarPerfil(){
        System.out.println("============================");
        System.out.println("    Perfil de Estudiante    ");
        System.out.println("============================");
        System.out.println(" ■ Nombre: " + nombre);
        System.out.println(" ■ Ficha: " + ficha);
        System.out.println(" ■ Trimestre: " + trimestre);
        System.out.println(" ■ Promedio: " + promedio);
        System.out.println("============================");
    }
}