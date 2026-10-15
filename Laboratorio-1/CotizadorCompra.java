// 1. Convertir los porcentajes ingresados (enteros) a proporción decimal dividiendo por 100.0.
// 2. Calcular el subtotal multiplicando el precio unitario por la cantidad.
// 3. Calcular el valor del descuento multiplicando el subtotal por el porcentaje de descuento en decimal.
// 4. Calcular la base restando el valor del descuento al subtotal.
// 5. Calcular el valor del impuesto multiplicando la base (después de descuento) por el porcentaje de impuesto en decimal.
// 6. Calcular el total final sumando la base y el valor del impuesto.



// import java.util.Scanner;
// import java.util.Locale;

// public class CotizadorCompra {

//     public static void main(String[] args) {
        
//         Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

//         System.out.println("=============================");
//         System.out.println("       COTIZADOR JAVA        ");
//         System.out.println("=============================\n");

        
//         System.out.print("Nombre del cliente: ");
//         String nombreCliente = scanner.nextLine();

//         System.out.print("Producto: ");
//         String producto = scanner.nextLine();

//         System.out.print("Precio unitario: ");
//         double precioUnitario = scanner.nextDouble();

//         System.out.print("Cantidad: ");
//         int cantidad = scanner.nextInt();

//         System.out.print("Descuento (%): ");
//         int pctDescuentoEntero = scanner.nextInt();

//         System.out.print("Impuesto (%): ");
//         int pctImpuestoEntero = scanner.nextInt();

        
        
//         double descuentoDecimal = pctDescuentoEntero / 100.0;
//         double impuestoDecimal = pctImpuestoEntero / 100.0;

        
//         double subtotal = precioUnitario * cantidad;
//         double valorDescuento = subtotal * descuentoDecimal;
//         double base = subtotal - valorDescuento;
//         double valorImpuesto = base * impuestoDecimal;
//         double total = base + valorImpuesto;

      
//         System.out.println("\n-----------------------------");
//         System.out.println("RESUMEN DE COMPRA");
//         System.out.println("-----------------------------");
//         System.out.println("Cliente: " + nombreCliente);
//         System.out.println("Producto: " + producto);
//         System.out.println("Precio unitario: $" + precioUnitario);
//         System.out.println("Cantidad: " + cantidad);
//         System.out.println("Subtotal: $" + subtotal);
//         System.out.println("Descuento: $" + valorDescuento);
//         System.out.println("Base: $" + base);
//         System.out.println("Impuesto: $" + valorImpuesto);
//         System.out.println("TOTAL: $" + total);

//         scanner.close();
//     }
// }

// Revisión del código
// Antes de terminar revisa:

// ¿los nombres de las variables permiten entender qué almacenan?
// ¿cada dato tiene un tipo adecuado?
// ¿los cálculos están separados en variables?
// ¿el usuario puede cambiar todos los datos sin editar el código?
// ¿la salida permite entender cómo se obtuvo el total?
// ¿el programa compila sin advertencias o errores que no comprendas?


// ¿Para qué sirve Scanner?
// Serviría para leer e interpretar los datos que el usuario ingresa desde la consola o terminal mientras el programa se está ejecutando.
// ¿Qué diferencia hay entre nextLine(), nextInt() y nextDouble()?
// nextLine(): Lo uso para leer cadenas de texto completas, incluyendo espacios (por ejemplo, nombres o productos).
// nextInt(): Lo uso para capturar exclusivamente números enteros, como la cantidad de items o la edad.
// nextDouble(): Lo uso cuando necesito leer números que tienen parte decimal, como precios o valores de impuestos.
// ¿or qué a veces se usa un nextLine() adicional después de leer un número?
// Porque cuando uso nextInt() o nextDouble(), el método solo consume el valor numérico y deja el salto de línea (el "Enter") guardado en el búfer. Si después ejecuto un nextLine(), este lee ese "Enter" que quedó pendiente y se pasa de largo sin dejar ingresar nada. Poner un nextLine() adicional ayuda a limpiar el búfer y consumir ese "Enter".
// ¿Qué ventaja tiene pedir los datos al usuario en lugar de escribirlos directamente en el código?
// La ventaja principal es que el programa se vuelve dinámico y reutilizable. No tengo que modificar el código fuente ni volver a compilar 
// cada vez que quiera probar con valores, clientes o precios diferentes.










// import java.util.Scanner;
// import java.util.Locale;

// public class CotizadorCompra {

//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

//         System.out.println("=============================");
//         System.out.println("       COTIZADOR JAVA        ");
//         System.out.println("=============================\n");

//         // --- ENTRADA DE DATOS DEL VENDEDOR Y COTIZACIÓN ---
//         System.out.print("Nombre del vendedor: ");
//         String nombreVendedor = scanner.nextLine();

//         System.out.print("Código de la cotización: ");
//         String codigoCotizacion = scanner.nextLine();

//         // --- ENTRADA DE DATOS DEL CLIENTE Y PRODUCTO ---
//         System.out.print("Nombre del cliente: ");
//         String nombreCliente = scanner.nextLine();

//         System.out.print("Producto: ");
//         String producto = scanner.nextLine();

//         System.out.print("Precio unitario: ");
//         double precioUnitario = scanner.nextDouble();

//         System.out.print("Cantidad: ");
//         int cantidad = scanner.nextInt();

//         System.out.print("Descuento (%): ");
//         int pctDescuentoEntero = scanner.nextInt();

//         System.out.print("Impuesto (%): ");
//         int pctImpuestoEntero = scanner.nextInt();

//         System.out.print("Costo de envío: ");
//         double costoEnvio = scanner.nextDouble();

//         // --- CÁLCULOS PASO A PASO ---
        
//         // Conversión de porcentajes a decimal
//         double descuentoDecimal = pctDescuentoEntero / 100.0;
//         double impuestoDecimal = pctImpuestoEntero / 100.0;

//         // Operaciones aritméticas guardadas en variables separadas
//         double subtotal = precioUnitario * cantidad;
//         double valorDescuento = subtotal * descuentoDecimal;
//         double base = subtotal - valorDescuento;
//         double valorImpuesto = base * impuestoDecimal;
        
//         // El envío se suma al final, después de calcular el impuesto
//         double totalFinal = base + valorImpuesto + costoEnvio;

//         // --- RESUMEN FINAL ---
//         System.out.println("\n-----------------------------");
//         System.out.println("RESUMEN DE COMPRA");
//         System.out.println("-----------------------------");
//         System.out.println("Código Cotización: " + codigoCotizacion);
//         System.out.println("Vendedor:          " + nombreVendedor);
//         System.out.println("Cliente:           " + nombreCliente);
//         System.out.println("Producto:          " + producto);
//         System.out.println("Precio unitario:   $" + precioUnitario);
//         System.out.println("Cantidad:          " + cantidad);
//         System.out.println("-----------------------------");
//         System.out.println("Subtotal:          $" + subtotal);
//         System.out.println("Descuento:         $" + valorDescuento);
//         System.out.println("Base:              $" + base);
//         System.out.println("Impuesto:          $" + valorImpuesto);
//         System.out.println("Costo de envío:    $" + costoEnvio);
//         System.out.println("-----------------------------");
//         System.out.println("TOTAL FINAL:       $" + totalFinal);

//         scanner.close();
//     }
// }

