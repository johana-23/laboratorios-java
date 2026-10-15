public class AccesoActividad {
    public static void main(String[] args) {
        int edad = 15;
        boolean inscripcion = true;
        boolean documentoPresentado = true;

        boolean permitido = edad >= 18;
        boolean permitido2 = inscripcion && documentoPresentado;
        boolean permitido3 = edad >= 18 && inscripcion && documentoPresentado;

        System.out.println(permitido);
        System.out.println(permitido2); 
        System.out.println(permitido3);
    }
    
}


