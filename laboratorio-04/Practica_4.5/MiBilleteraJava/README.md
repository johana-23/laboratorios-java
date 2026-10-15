# Mi Billetera Java 2.0

Aplicación de consola que registra ingresos y gastos con categoría. Cada operación válida genera un
`Movimiento` guardado en un historial. Permite consultar el historial, filtrar por tipo, ver las
categorías utilizadas y obtener un informe de gastos agrupados. No usa archivos ni base de datos:
los datos viven solo mientras el programa está en ejecución.

## Clases

| Archivo | Propósito |
|---|---|
| `Movimiento.java` | Registro inmutable (tipo, categoría, valor). Valida al construirse y lanza `IllegalArgumentException` si los datos son inválidos. |
| `Billetera.java` | Saldo, contadores, acumulados e historial. Contiene todas las reglas de negocio. No usa `Scanner` ni imprime. |
| `Main.java` | Menú, lectura de datos con `nextLine()` y presentación de resultados. Atiende entradas incorrectas con `try/catch`. |
| `PruebaBilletera.java` | Pruebas automáticas (casos obligatorios y prueba numérica del informe). |
| `PruebaBilleteras.java` | Independencia entre dos billeteras y comprobación de la copia del historial. |

## Compilar y ejecutar

```
javac -encoding UTF-8 *.java
java Main
java PruebaBilletera
java PruebaBilleteras
```

## Colecciones utilizadas

- **`ArrayList<Movimiento>`**: el historial. Conserva el orden de registro, permite repetidos y acceso por índice.
- **`HashSet<String>`**: categorías únicas. Solo importa que no se repitan; no garantiza orden.
- **`HashMap<String, Double>`**: total gastado por categoría (clave = categoría, valor = acumulado con `getOrDefault`).

## Casos de prueba y resultados observados

Todos se ejecutan con `java PruebaBilletera` y pasan (ver salida `OK`):

| Prueba | Resultado |
|---|---|
| Billetera nueva | Saldo 0, historial vacío |
| Ingreso 200000 | Saldo 200000, 1 movimiento |
| Gasto 50000 | Saldo 150000, 2 movimientos |
| Gasto 300000 | Rechazado, sin cambios |
| Ingreso 0 / gasto negativo | Rechazados |
| Categoría vacía o solo espacios | Rechazada |
| NaN / Infinity | Rechazados |
| Filtro INGRESO / GASTO | Solo el tipo pedido |
| Categorías | Cada una aparece una vez |
| Informe numérico | 6 movimientos, ingresado 1600000.0, gastado 135000.0, saldo 1465000.0, 5 categorías |
| Gastos agrupados | Transporte 50000.0, Alimentación 45000.0, Salud 40000.0 (suma = total gastado) |
| Dos billeteras | Historiales independientes |
| Copia del historial | `clear()` en la copia no afecta el original |

Desde el menú (`java Main`): texto como opción o monto muestra un mensaje y el menú continúa;
una opción desconocida muestra un aviso; la opción 0 finaliza.
