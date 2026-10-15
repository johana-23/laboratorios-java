package Libro;

public class Main {
    public static void main(String[] args){
        Libro libro1 = new Libro();

        libro1.titulo= "La Divina Comedia";
        libro1.autor= "Dante Alighieri";
        libro1.paginas= 800;

        /* ------------------------------------- */
        Libro libro2 = new Libro();

        libro2.titulo= "Harry Potter y la Piedra Filosofal";
        libro2.autor= "J.K. Rowling";
        libro2.paginas= 288;

        System.out.println("=========================");
        libro1.mostrarFicha();
        System.out.println("=========================");
        libro2.mostrarFicha();
        System.out.println("=========================");
    }
}


