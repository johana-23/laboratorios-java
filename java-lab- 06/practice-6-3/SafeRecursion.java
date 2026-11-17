public class SafeRecursion {

    static void countDown(int value) {
        System.out.println("Entering: " + value);

        if (value == 0) {
            System.out.println("Base case");
            return;
        }

        countDown(value - 1);
        System.out.println("Returning: " + value);
    }

    public static void main(String[] args) {
        countDown(2);
    }
}

/* static void displayFirst(int number) {
    System.out.println(number);
}

static void displaySecond(int number) {
    System.out.println(number);
} 
    Estos dos métodos podrían existir en una misma clase:
    Porque Java permite declarar varios métodos dentro de una misma clase, siempre que sus declaraciones no entren en conflicto. En este caso, los nombres son diferentes, por lo que no existe un conflicto.*/