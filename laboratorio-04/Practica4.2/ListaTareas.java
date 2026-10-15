import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListaTareas {
    public static void main(String[] args) {
        
        List<String> tareas = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese 4 tareas: ");
        String tarea1 = scanner.nextLine();
        String tarea2 = scanner.nextLine();
        String tarea3 = scanner.nextLine();
        String tarea4 = scanner.nextLine();

        tareas.add(tarea1);
        tareas.add(tarea2);
        tareas.add(tarea3);
        tareas.add(tarea4);

        System.out.println("Tareas ingresadas:");

        System.out.println(tareas);
        
        System.out.println("Tareas numeradas:");
        for (int i = 0; i < tareas.size(); i++) {
            System.out.println((i + 1) + ". " + tareas.get(i));
        }


        System.out.println("Cantidad: " + tareas.size());

        System.out.println("Ingrese una tarea para  comprobar si existe: ");

        String existenciaTarea = scanner.nextLine();
        System.out.println("La tarea' " + existenciaTarea + "' existe en la lista: " + tareas.contains(existenciaTarea));
        
        
         System.out.println("Ingrese una tarea para eliminar: ");

        String eliminacionTarea = scanner.nextLine();
       if (tareas.contains(eliminacionTarea)){
            tareas.remove(eliminacionTarea);
            System.out.println("Tarea eliminada correctamente.");
        } 
        
        else {
            System.out.println("La tarea no se encontró en la lista.");
        }

        System.out.println("lista de tarea actualizada "+ tareas);
        scanner.close();
    }
    
}
