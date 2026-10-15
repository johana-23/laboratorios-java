package RegistroEstudiantes;
public class Main {
    public static void main(String[] args){
        Estudiante Est1 = new Estudiante("Juan Perez", "883425", 3, 4.1);
        Estudiante Est2 = new Estudiante("Julian Gomez", "883425", 3, 2.8);

        Est1.mostrarPerfil();
        System.out.println(" Estado: ");
        System.out.println("============================");

        System.out.println("");

        Est2.mostrarPerfil();
        System.out.println(" Estado: ");
        System.out.println("============================");

    }
}
