# Laboratorio 2 - Condiciones, ciclos y metodos

Segundo laboratorio. Continua el repositorio del Laboratorio 01 y agrega decisiones (`if`, `else if`, `else`),
comparaciones, ciclos (`for`, `while`, `do while`), menus con `switch` y metodos estaticos. Termina con el
reto integrador **`BilleteraConsola.java`**.

## Estructura

| Carpeta | Contenido |
|---|---|
| `comparaciones-booleanas/` | Operadores relacionales y logicos, validaciones |
| `decisiones-if-else/` | Clasificar notas y numeros, descuentos, acceso, edad |
| `repeticion-ciclos/` | Contadores, tablas, acumuladores, menus con ciclo |
| `seleccion-switch/` | Menus y conversores con `switch` |
| `metodos-billetera-consola/` | Metodos con parametros y retorno + **`BilleteraConsola.java`** |

## BilleteraConsola

Menu repetitivo (`do while`) con: 1. Registrar ingreso, 2. Registrar gasto, 3. Consultar saldo,
4. Mostrar resumen, 0. Salir.

- El ingreso debe ser mayor a cero.
- El gasto debe ser mayor a cero y no puede superar el saldo disponible.
- Los metodos estaticos reciben el saldo como parametro y lo devuelven actualizado.
- Una opcion inexistente muestra "Opcion no valida" y el menu continua.

## Como compilar y ejecutar

```
cd laboratorio-2/metodos-billetera-consola
javac -encoding UTF-8 BilleteraConsola.java
java BilleteraConsola
```
