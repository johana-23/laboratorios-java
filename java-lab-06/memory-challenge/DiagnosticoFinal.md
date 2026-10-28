Diagnóstico final — Respuestas para la comprobación oral
1. ¿Qué contiene el JDK que permite compilar?
Contiene herramientas de desarrollo, incluido el compilador javac, que transforma el código fuente .java en bytecode.
2. ¿Para qué sirve una JVM al ejecutar un archivo .class?
Sirve para ejecutar el bytecode de Java en un entorno compatible, administrando aspectos como la ejecución de instrucciones y la memoria.
3. ¿Qué diferencia hay entre java y javac?
javac compila el código fuente. java inicia la ejecución de una aplicación Java.
4. ¿Qué muestra javap -c?
Muestra las instrucciones de bytecode de los métodos de una clase compilada.
5. ¿Qué es un stack frame?
Es el marco de pila que se crea para una invocación de método. Contiene información necesaria para esa ejecución, como variables locales y datos para continuar después de una llamada.
6. ¿Qué diferencia hay entre pila de llamadas y pila de operandos?
La pila de llamadas mantiene los marcos de las invocaciones de métodos. La pila de operandos se utiliza dentro de cada marco para realizar operaciones con valores, como sumar números o preparar argumentos.
7. ¿En qué área define la JVM la asignación de memoria de instancias y arreglos?
Las instancias de clases y los arreglos se asignan en el heap o montón de la JVM.
8. ¿Cuál es la diferencia entre una variable int y una variable PetProfile?
Una variable int almacena un valor entero. Una variable PetProfile almacena un valor de referencia que permite acceder a un objeto de esa clase, o puede contener null.
9. ¿Qué significa first == second entre referencias?
Comprueba si ambas referencias apuntan al mismo objeto, o si ambas son null. No compara automáticamente el contenido de los objetos.
10. ¿Por qué un alias permite observar cambios en un mismo objeto?
Porque el alias y la referencia original apuntan al mismo objeto. Si una referencia modifica un atributo, el cambio puede observarse mediante la otra.
11. ¿Por qué reasignar un parámetro no reasigna una variable del llamador?
Porque Java pasa los argumentos por valor. El parámetro recibe una copia del valor de referencia, y reasignar esa copia no modifica la variable original.
12. ¿Cuál es la salida exacta de MemoryDemo.java y por qué?
Para la versión del ejercicio anterior, la salida esperada es:
Dentro: Otra
Fuera: Sol


Dentro del método, el parámetro se reasigna a un objeto nuevo llamado "Otra". La variable del llamador continúa apuntando al objeto original, cuyo nombre se cambió a "Sol".
13. ¿Qué significa que un objeto sea elegible para GC?
Significa que el objeto ya no puede alcanzarse desde las referencias activas relevantes. La JVM puede recolectarlo, pero no necesariamente lo hace de inmediato.
14. ¿Por qué una referencia null puede ocasionar un error en tiempo de ejecución?
Porque no apunta a ningún objeto. Si intentamos invocar un método a través de esa referencia, se produce una NullPointerException.
15. ¿Cómo observarías con el depurador el orden de llamadas y retornos?
Colocaría puntos de interrupción en las llamadas a los métodos, iniciaría la depuración y observaría el Call Stack. Utilizaría Step Into para entrar en un método y Step Out para regresar al método que lo llamó.


Microejercicio — Predice la salida
Código:
int[] values = {3, 6};
int[] other = values;

other[1] = 9;

System.out.println(values[1]);
System.out.println(values == other);


Salida esperada:
9
true


Explicación:

other = values hace que ambas variables apunten al mismo arreglo. Por eso, al cambiar other[1] a 9, también cambia el valor que se consulta mediante values[1].

La comparación values == other devuelve true porque ambas referencias apuntan al mismo arreglo.