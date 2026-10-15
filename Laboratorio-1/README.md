# Laboratorio 01 - Fundamentos de Java y cotizador de compra

Primer laboratorio del proceso formativo en Java. Contiene los ejercicios de variables, tipos de datos,
operadores, lectura con `Scanner` y salida por consola, y el programa integrador **`CotizadorCompra.java`**.

## Contenido

| Archivo | Que practica |
|---|---|
| `HolaJava.java`, `Saludo.java`, `Presentacion.java` | Estructura de un programa y `System.out` |
| `DatosBasicos.java`, `DatosUsuario.java` | Variables y tipos de datos |
| `Operadores.java`, `CalculoCompra.java`, `CalculoPromedio.java`, `CalculoSalario.java`, `CalculoSueldo.java`, `FacturaSimple.java`, `Inventario.java` | Operadores aritmeticos y calculos paso a paso |
| `ConversionTiempo.java`, `ConversorTemperatura.java` | Conversiones |
| `EntradaNumeros.java`, `LecturasMixtas.java`, `SumaInteractiva.java` | Lectura con `Scanner` (`nextInt`, `nextDouble`, `nextLine`) |
| **`CotizadorCompra.java`** | Programa integrador: subtotal, descuento, base, impuesto, envio y total |
| `prueba.java` | Archivo de prueba de estructura de clases |



## Como compilar y ejecutar

Requisito: JDK 21 o superior (`javac -version`).

```
cd laboratorio-1
javac CotizadorCompra.java
java CotizadorCompra
```

El programa pide: vendedor, codigo de cotizacion, cliente, producto, precio unitario, cantidad,
descuento (%), impuesto (%) y costo de envio. Los porcentajes se escriben como enteros (`15` = 15 %) y se
convierten a decimal (`15 / 100.0 = 0.15`). Para los decimales se usa punto: `2500000.50`.

Cualquier otro ejercicio se ejecuta igual: `javac Nombre.java` y luego `java Nombre`.
