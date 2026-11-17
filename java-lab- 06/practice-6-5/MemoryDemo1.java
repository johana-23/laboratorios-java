/** Java passes every argument by value, including reference values. */
public class MemoryDemo1 {
    static final class Pet {
        String name;

        Pet(String name) {
            this.name = name;
        }
    }

    static void change(Pet local) {
        
        local.name = "Sol"; // Same object, visible to caller.
        local = new Pet("Otra"); // Only the local reference value changes.
        System.out.println("Dentro: " + local.name);
    }

    public static void main(String[] args) {
        Pet original = new Pet("Luna");
        Pet alias = original;
        change(original);
        original = createPet();
        clearLocalReference(original);

        System.out.println("Alias: " + alias.name);
        System.out.println("Nombre: " + original.name);
        
        System.out.println("Fuera: " + original.name);
        if (!original.name.equals("Sol")) throw new AssertionError("Paso por valor");
        System.out.println(original == null);
    }

    static Pet createPet() {
        return new Pet("Otra");
        }

    static void clearLocalReference(Object value) {
    value = null;
}
}