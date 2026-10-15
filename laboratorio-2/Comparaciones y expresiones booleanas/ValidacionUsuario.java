public class ValidacionUsuario {
    public static void main(String[] args) {
     String usuarioRegistrado = "aprendiz";
    String usuarioIngresado = "Aprendiz";

    System.out.println(usuarioRegistrado.equals(usuarioIngresado));
    System.out.println(usuarioIngresado.equals(usuarioIngresado));
    System.out.println(usuarioIngresado.equalsIgnoreCase(usuarioIngresado));
    }
//  observe que cuando uno pone el nombre de usuario con mayúscula y el otro con minúscula, el resultado es falso, pero si se pone equalsIgnoreCase, el resultado es verdadero porque no distingue entre mayúsculas y minúsculas.

}
