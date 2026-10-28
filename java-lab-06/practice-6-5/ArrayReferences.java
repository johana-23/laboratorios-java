public class ArrayReferences {

    static void updateFirst(int[] values) {
        values[0] = 99;
    }

    static void replaceArray(int[] values) {
        values = new int[]{7, 8};
        System.out.println("Inside: " + values[0]);
    }

    public static void main(String[] args) {
        int[] numbers = {1, 2};

        updateFirst(numbers);
        replaceArray(numbers);

        System.out.println("Outside: " + numbers[0] + ", " + numbers[1]);
    }
}

/* ¿ updateFirstModifica el arreglo del objeto que también utiliza main?
si ya que el método updateFirst recibe una referencia al arreglo original y modifica su primer elemento directamente.
¿ replaceArray modifica la variable numbersdel llamador?no modifica la variable numbers del llamador, ya que dentro de replaceArray se crea un nuevo arreglo y se asigna a la variable local values, sin afectar la referencia original en main.
¿Cuántos arreglos hay después de crear el segundo arreglo? Hay dos arreglos: el original y el nuevo creado en replaceArray.
¿Qué se imprimirá dentro y fuera? Dentro se imprimirá 7, y fuera se imprimirá 99, 2. */