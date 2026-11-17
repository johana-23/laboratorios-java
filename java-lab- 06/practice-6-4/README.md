first       ----> Objeto A: Pet(name = "Luna")
alias       ----> Objeto A: Pet(name = "Luna")
independent ----> Objeto B: Pet(name = "Luna")

¿Cuáles variables están conectadas al mismo objeto?  la variables conectafo directamente es A y A ¿Qué nombres tendrán los objetos después de alias.rename("Sol")? ahora pasaran a llamarse Sol

Predice el tipo de error. el error es que el objeto no se refiere  a ningun objeto.
 Ejecútalo una vez, identifica la línea en el stack trace y después corrige el acceso. ¿Por qué compiló un programa que falló durante la ejecución? fallo ya que es un objeto null  el cual no apunta a ninguna ubicación válida osea no se refiere a ningún objeto y el compilador no impide necesariamente que se invoque un método mediante una referencia que pueda ser null. El problema aparece cuando el programa intenta ejecutar esa instrucción sin tener un objeto al cual acceder.
       VARIABLES                 OBJETOS

       first ──────────┐
                       │
       alias ──────────┼───────> Objeto 1
                       │          Pet
       third ──────────┘          name = "Luna"


       independent ────────────> Objeto 2
                                  Pet
                                  name = "Luna"

Después de ejecutar alias = new Pet("Nova"); y alias.rename("Max");

       VARIABLES                 OBJETOS

       first ──────────┐
                       │
       third ──────────┴───────> Objeto 1
                                  Pet
                                  name = "Luna"


       alias ──────────────────> Objeto 3
                                  Pet
                                  name = "Max"


       independent ────────────> Objeto 2
                                  Pet
                                  name = "Luna"

El nombre del objeto A no se modifica con alias.rename("Max") porque, después de ejecutar alias = new Pet("Nova"), la variable alias deja de apuntar al objeto A y pasa a apuntar a un objeto nuevo.

Por eso, cuando se ejecuta alias.rename("Max"), solo se modifica el nombre del nuevo objeto. El objeto A conserva su nombre "Luna", porque las referencias first y third siguen apuntando a él.


 
 Explica sin confundir estos conceptos: valor primitivo: Es un valor que se almacena directamente en una variable de tipo primitivo, como int, double, char o boolean.
 valor de referencia: Es un valor que permite acceder a un objeto. Las variables de tipos como Pet, String y los arreglos almacenan referencias, no el objeto directamente.
  objeto: Es una entidad creada en memoria a partir de una clase. Tiene un estado, representado por sus atributos, y puede ofrecer comportamientos mediante sus métodos.

  instancia independiente: Es un objeto diferente de los demás, aunque se haya creado a partir de la misma clase y tenga los mismos valores.

  alias: Es una referencia adicional que apunta al mismo objeto que otra variable.
  referencia null: Es una referencia que no apunta a ningún objeto.
  elegibilidad para GC: GC significa Garbage Collector o recolector de basura. Un objeto es elegible para la recolección de basura cuando ya no puede ser alcanzado desde las referencias activas consideradas por la JVM