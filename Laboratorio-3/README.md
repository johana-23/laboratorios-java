# Laboratorio 3 - Programacion orientada a objetos (MiBilleteraJava)

Tercer laboratorio. La billetera procedural del Laboratorio 02 se transforma en una solucion con clases y objetos.
El Laboratorio 01 y el 02 se conservan sin cambios.

## Estructura

| Ruta | Contenido |
|---|---|
| `practicas/Seccion_3_1` a `Seccion_3_4` | Ejercicios de clases, atributos, constructores, `this`, `static` y objetos independientes |
| `MiBilleteraJava/Billetera.java` | Clase del modelo: atributos privados, constructor, metodos publicos, validaciones, contadores y acumulados |
| `MiBilleteraJava/Main.java` | Menu de consola; solo lee datos y muestra resultados |
| `MiBilleteraJava/PruebaBilleteras.java` | Demuestra que dos billeteras tienen estados independientes |

## Responsabilidades

- **`Billetera`**: guarda el estado (titular, saldo, cantidad y total de ingresos y gastos) y contiene todas las reglas.
  `registrarIngreso` y `registrarGasto` devuelven `true` si la operacion fue aceptada y `false` si fue rechazada;
  contadores y acumulados solo cambian cuando se acepta.
- **`Main`**: interaccion con el usuario (`Scanner`, menu, mensajes). No modifica el saldo directamente.
- **`PruebaBilleteras`**: verificacion automatica de independencia entre objetos.

## Como compilar y ejecutar

```
cd laboratorio-3/MiBilleteraJava
javac -encoding UTF-8 *.java
java Main
java PruebaBilleteras
```
