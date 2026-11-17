
public class ObjectReferences1 {

    static final class Pet {
        private String name;

        Pet(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void rename(String newName) {
            name = newName;
        }
    }

    public static void main(String[] args) {
        Pet first = new Pet("Luna");
        Pet alias = first;
        Pet independent = new Pet("Luna");

        // 1. Crear una tercera referencia al mismo objeto
        Pet third = first;

        // 2. Comprobar el nombre de third
        System.out.println("Third name: " + third.getName());

        // 3. Reasignar alias a un objeto nuevo
        alias = new Pet("Nova");

        // 4. Cambiar el nombre del objeto al que apunta alias
        alias.rename("Max");

        // 5. Consultar las cuatro referencias
        System.out.println("First name: " + first.getName());
        System.out.println("Third name: " + third.getName());
        System.out.println("Independent name: " + independent.getName());
        System.out.println("Alias name: " + alias.getName());

        Pet missing = null;
        

        if (missing != null) {
        System.out.println(missing.getName());
        } else {
            System.out.println("La referencia no apunta a ningún objeto.");
        }

    }
}

/* Predice el tipo de error. el error es que el objeto no se refiere  a ningun objeto.
 Ejecútalo una vez, identifica la línea en el stack trace y después corrige el acceso. ¿Por qué compiló un programa que falló durante la ejecución? fallo ya que es un objeto null  el cual no apunta a ninguna ubicación válida osea no se refiere a ningún objeto y el compilador no impide necesariamente que se invoque un método mediante una referencia que pueda ser null. El problema aparece cuando el programa intenta ejecutar esa instrucción sin tener un objeto al cual acceder.
 
 Explica sin confundir estos conceptos: valor primitivo: Es un valor que se almacena directamente en una variable de tipo primitivo, como int, double, char o boolean.
 valor de referencia: Es un valor que permite acceder a un objeto. Las variables de tipos como Pet, String y los arreglos almacenan referencias, no el objeto directamente.
  objeto: Es una entidad creada en memoria a partir de una clase. Tiene un estado, representado por sus atributos, y puede ofrecer comportamientos mediante sus métodos.

  instancia independiente: Es un objeto diferente de los demás, aunque se haya creado a partir de la misma clase y tenga los mismos valores.

  alias: Es una referencia adicional que apunta al mismo objeto que otra variable.
  referencia null: Es una referencia que no apunta a ningún objeto.
  elegibilidad para GC: GC significa Garbage Collector o recolector de basura. Un objeto es elegible para la recolección de basura cuando ya no puede ser alcanzado desde las referencias activas consideradas por la JVM
 
 */
