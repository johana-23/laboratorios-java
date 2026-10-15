// import java.util.Scanner;

// public class DiaSemana {

//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in);
//         System.out.println("Ingrese un número del 1 al 7");
//         int dia = scanner.nextInt();

//         switch (dia) {
//             case 1:
//                 System.out.println("Lunes");
//                 break;
//             case 2:
//                 System.out.println("Martes");
//                 break;
//             case 3:
//                 System.out.println("Miércoles");
//                 break;
//             case 4:
//                 System.out.println("Jueves");
//                 break;
//             case 5:
//                 System.out.println("Viernes");
//                 break;
//             case 6:
//                 System.out.println("Sábado");
//                 break;
//             case 7:
//                 System.out.println("Domingo");
//                 break;
//             default:
//                 System.out.println("Día no válido");
//                 break;
//         }

//         scanner.close();
//     }
// }




import java.util.Scanner;

public class DiaSemana {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese un letra  del  abecedario A al G");
        String dia = scanner.nextLine();

        switch (dia) {
            case "A":
                System.out.println("Lunes");
                break;
            case "B":
                System.out.println("Martes");
                break;
            case "C":
                System.out.println("Miércoles");
                break;
            case "D":
                System.out.println("Jueves");
                break;
            case "E":
                System.out.println("Viernes");
                break;
            case "F":
                System.out.println("Sábado");
                break;
            case "G":
                System.out.println("Domingo");
                break;
            default:
                System.out.println("Día no válido");
                break;
        }

        scanner.close();
    }
}




