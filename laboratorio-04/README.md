# Laboratorio 04 - Colecciones, historial y excepciones

Cuarto laboratorio. Evoluciona la billetera orientada a objetos del Laboratorio 03 con un historial de
movimientos, colecciones, reportes y manejo de excepciones. Los laboratorios anteriores se conservan.

## Estructura

| Ruta | Contenido |
|---|---|
| `practicas/Practica4.1` | Arreglos |
| `practicas/Practica4.2` | `ArrayList` y la clase `Movimiento` |
| `practicas/Practica4.3` | `Set`, `Map` y reportes |
| `practicas/Practica_4.4` | `try/catch`, `NumberFormatException`, `finally`, validaciones |
| `practicas/Depuracion` | Siete errores tipicos de depuracion, con su correccion |
| `MiBilleteraJava/` | Proyecto final (`Main`, `Billetera`, `Movimiento` + pruebas + su propio README) |

## Colecciones usadas

- `ArrayList<Movimiento>`: historial en orden, con repetidos.
- `HashSet<String>`: categorias unicas.
- `HashMap<String, Double>`: gasto acumulado por categoria.

El historial solo recibe movimientos validos, `getMovimientos()` devuelve una copia (el historial interno no se
puede alterar desde fuera) y las entradas incorrectas se atienden con `try/catch`.

## Como compilar y ejecutar

```
cd laboratorio-04/MiBilleteraJava
javac -encoding UTF-8 *.java
java Main
java PruebaBilletera
java PruebaBilleteras
```
