import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String nombre = "";
        while (nombre.isBlank()) {
            System.out.print("Titular: ");
            nombre = scanner.nextLine().trim();
            if (nombre.isBlank()) {
                System.out.println("El titular no puede estar vacío.");
            }
        }
        Billetera billetera = new Billetera(nombre);

        boolean salir = false;
        while (!salir) {
            mostrarMenu(billetera);
            System.out.print("Opción: ");
            String entrada = scanner.nextLine();

            try {
                int opcion = Integer.parseInt(entrada.trim());

                switch (opcion) {
                    case 1 -> registrarIngreso(scanner, billetera);
                    case 2 -> registrarGasto(scanner, billetera);
                    case 3 -> System.out.println("Saldo actual: $" + billetera.getSaldo());
                    case 4 -> mostrarMovimientos(billetera);
                    case 5 -> consultarPorTipo(scanner, billetera);
                    case 6 -> mostrarCategorias(billetera);
                    case 7 -> mostrarResumen(billetera);
                    case 0 -> {
                        System.out.println("¡Hasta pronto!");
                        salir = true;
                    }
                    default -> System.out.println("La opción " + opcion + " no existe.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar una opción numérica.");
            }
        }

        scanner.close();
    }

    private static void mostrarMenu(Billetera b) {
        System.out.println();
        System.out.println("================================");
        System.out.println("       MI BILLETERA JAVA");
        System.out.println("================================");
        System.out.println("Titular: " + b.getTitular());
        System.out.println("Saldo disponible: $" + b.getSaldo());
        System.out.println();
        System.out.println("1. Registrar ingreso");
        System.out.println("2. Registrar gasto");
        System.out.println("3. Consultar saldo");
        System.out.println("4. Mostrar movimientos");
        System.out.println("5. Consultar movimientos por tipo");
        System.out.println("6. Consultar categorías utilizadas");
        System.out.println("7. Resumen financiero");
        System.out.println("0. Salir");
    }

    /** Devuelve el monto leído o null si la entrada no es un monto válido (ya informó el error). */
    private static Double leerMonto(Scanner scanner) {
        System.out.print("Valor: ");
        String entrada = scanner.nextLine();
        try {
            double valor = Double.parseDouble(entrada.trim());
            if (!Double.isFinite(valor) || valor <= 0) {
                System.out.println("El monto debe ser positivo y finito.");
                return null;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Debes escribir un monto numérico.");
            return null;
        }
    }

    private static void registrarIngreso(Scanner scanner, Billetera billetera) {
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        Double valor = leerMonto(scanner);
        if (valor == null) {
            return;
        }
        if (billetera.registrarIngreso(valor, categoria)) {
            System.out.println("Ingreso registrado.");
        } else {
            System.out.println("Ingreso rechazado: revisa la categoría y el monto.");
        }
    }

    private static void registrarGasto(Scanner scanner, Billetera billetera) {
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        Double valor = leerMonto(scanner);
        if (valor == null) {
            return;
        }
        if (billetera.registrarGasto(valor, categoria)) {
            System.out.println("Gasto registrado.");
        } else {
            System.out.println("Gasto rechazado: categoría vacía o saldo insuficiente.");
        }
    }

    private static void mostrarMovimientos(Billetera billetera) {
        List<Movimiento> lista = billetera.getMovimientos();
        System.out.println("================================");
        System.out.println("           HISTORIAL");
        System.out.println("================================");
        if (lista.isEmpty()) {
            System.out.println("El historial está vacío.");
            return;
        }
        imprimirNumerados(lista);
    }

    private static void imprimirNumerados(List<Movimiento> lista) {
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ". " + lista.get(i));
        }
    }

    private static void consultarPorTipo(Scanner scanner, Billetera billetera) {
        System.out.print("Tipo (INGRESO o GASTO): ");
        String tipo = scanner.nextLine().trim().toUpperCase();

        if (!tipo.equals("INGRESO") && !tipo.equals("GASTO")) {
            System.out.println("Tipo no reconocido. Escribe INGRESO o GASTO.");
            return;
        }
        List<Movimiento> encontrados = billetera.buscarPorTipo(tipo);
        if (encontrados.isEmpty()) {
            System.out.println("No hay movimientos de tipo " + tipo + ".");
        } else {
            imprimirNumerados(encontrados);
        }
    }

    private static void mostrarCategorias(Billetera billetera) {
        Set<String> categorias = billetera.categoriasUtilizadas();
        if (categorias.isEmpty()) {
            System.out.println("Aún no hay categorías.");
            return;
        }
        System.out.println("Categorías utilizadas (" + categorias.size() + "):");
        for (String categoria : categorias) {
            System.out.println("- " + categoria);
        }
    }

    private static void mostrarResumen(Billetera b) {
        System.out.println("================================");
        System.out.println("       RESUMEN FINANCIERO");
        System.out.println("================================");
        System.out.println("Titular: " + b.getTitular());
        System.out.println("Saldo: $" + b.getSaldo());
        System.out.println("Ingresos realizados: " + b.getCantidadIngresos());
        System.out.println("Total ingresado: $" + b.getTotalIngresos());
        System.out.println("Gastos realizados: " + b.getCantidadGastos());
        System.out.println("Total gastado: $" + b.getTotalGastos());
        System.out.println();
        System.out.println("Gastos por categoría:");
        Map<String, Double> gastos = b.gastosPorCategoria();
        if (gastos.isEmpty()) {
            System.out.println("(sin gastos)");
        }
        for (Map.Entry<String, Double> entrada : gastos.entrySet()) {
            System.out.println(entrada.getKey() + ": $" + entrada.getValue());
        }
    }
}
