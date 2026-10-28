# Laboratorios Java

Repositorio personal del proceso formativo en Java. Cada laboratorio vive en su propia carpeta y se conserva
intacto cuando se agrega el siguiente, de modo que el historial de commits muestra la evolucion desde la primera
aplicacion de consola hasta la billetera con colecciones y, ahora, el estudio de la memoria de Java
(Stack, Heap y referencias).

Aprendiz: Johana Fernandez

Repositorio: <https://github.com/johana-23/laboratorios-java>

## Estado de los laboratorios

| Carpeta | Tema | Estado |
|---|---|---|
| [`Laboratorio-1`](Laboratorio-1/) | Fundamentos de Java, `Scanner`, operadores y `CotizadorCompra` | Completado |
| [`laboratorio-2`](laboratorio-2/) | Condiciones, ciclos, `switch`, metodos y `BilleteraConsola` | Completado |
| [`Laboratorio-3`](Laboratorio-3/) | POO: clases y objetos (`Seccion_3_1` a `Seccion_3_5`) | Completado |
| [`laboratorio-04`](laboratorio-04/) | Colecciones, historial (`Movimiento`), reportes, excepciones y `MiBilleteraJava` | Completado |
| [`java-lab-06`](java-lab-06/) | JDK/JRE/JVM, compilacion y ejecucion, Stack, Heap, referencias y paso de parametros | completado 

## Contenido de `java-lab-06`

| Carpeta | Tema | Archivos principales |
|---|---|---|
| [`practice-6-1`](java-lab-06/practice-6-1/) | Herramientas `java`, `javac`, `javap`; JDK y JVM; comprobaciones para Java 21 | `README.md` |
| [`practice-6-2`](java-lab-06/practice-6-2/) | Flujo de compilacion y ejecucion (`.java` -> `.class`), classpath (`-cp`) | `ExecutionFlow.java`, `ExecutionFlowError.java` |
| [`practice-6-3`](java-lab-06/practice-6-3/) | Pila de llamadas (Call Stack) y variables locales | `MethodStack.java`, `MethodStack1.java`, `SafeRecursion.java` |
| [`practice-6-4`](java-lab-06/practice-6-4/) | Objetos, alias, referencias independientes y `null` | `ObjectReferences.java`, `ObjectReferences1.java` |
| [`practice-6-5`](java-lab-06/practice-6-5/) | Paso de parametros por valor (primitivos, objetos y arreglos) | `MemoryDemo.java`, `MemoryDemo1.java`, `PrimitiveParameters.java`, `ArrayReferences.java` |
| [`memory-challenge`](java-lab-06/memory-challenge/) | Reto integrador de memoria y diagnostico final | `MemoryChallenge.java`, `PetProfile.java`, `MemoryChallengeCheck.java`, `DiagnosticoFinal.md` |

Algunas carpetas incluyen una segunda version del ejercicio terminada en `1` (por ejemplo `MemoryDemo1.java`)
donde se probaron variantes sin alterar el archivo original.

## Instrucciones de ejecucion

Requisito: JDK 21 o superior. Antes de empezar se comprueba con:

```
java -version
javac -version
```

Para que el bytecode generado sea compatible con Java 21 aunque el equipo tenga un JDK mas nuevo, se puede
compilar con `javac --release 21 ...`.

**Laboratorio-1:** `cd Laboratorio-1` -> `javac CotizadorCompra.java` -> `java CotizadorCompra`

**laboratorio-2 y Laboratorio-3:** consulta el `README.md` de cada carpeta.

**laboratorio-04:** `cd laboratorio-04/Practica_4.5/MiBilleteraJava` -> `javac -encoding UTF-8 *.java` -> `java Main`

**java-lab-06** (cada practica se ejecuta desde su propia carpeta):

| Practica | Compilar | Ejecutar |
|---|---|---|
| `practice-6-2` | `javac -d out ExecutionFlow.java` | `java -cp out ExecutionFlow` |
| `practice-6-3` | `javac -d out MethodStack.java` | `java -cp out MethodStack` |
| `practice-6-4` | `javac ObjectReferences1.java` | `java ObjectReferences1` |
| `practice-6-5` | `javac MemoryDemo.java` | `java MemoryDemo` |
| `memory-challenge` | `javac MemoryChallenge.java PetProfile.java` | `java MemoryChallenge` |

Si el programa sigue mostrando resultados antiguos, hay que volver a compilar: Java ejecuta el `.class`,
no el `.java`.

## Como se conserva la evolucion

- Cada laboratorio se agrega como una carpeta nueva; no se reescriben los anteriores.
- `java-lab-06` se incorpora igual que los demas: como carpeta nueva en la raiz.

Cada carpeta tiene su propio `README.md` con el detalle del laboratorio.