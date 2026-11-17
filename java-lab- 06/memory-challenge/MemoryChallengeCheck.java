/* public class MemoryChallengeCheck {

    public static void main(String[] args) {
        PetProfile original = new PetProfile("Luna");
        PetProfile alias = original;

        alias.registerVisit();
       

        PetProfile replacement = createReplacement(original);
        if (original.getVisits() != 1) {
            throw new AssertionError("Shared object was not updated");
        }

        PetProfile missing = null;
        missing.registerVisit();

        PetProfile independent = original;

        if (original == independent) {
            throw new AssertionError("Distinct objects share an identity");
        }

        if (independent.getVisits() != 0) {
            throw new AssertionError("Independent state was unexpectedly modified");
        }

        System.out.println("All reference checks passed");

        PetProfile missing = null;

        if (missing != null) {
            missing.registerVisit();
        } else {
            System.out.println("La referencia es nula.");
        }


        if (replacement == original) {
        throw new AssertionError("Replacement should be a different object");
        }
    }
    private static PetProfile  createReplacement(PetProfile previous) {
    return new PetProfile("Max");
}
} */

/* Error A — Referencia compartida accidental. En la clase de comprobación, reemplace la creación de independentpor una asignación que reutilice original. Comprueba qué aserción falla y por qué.

Se lanza AssertionError porque original == independent es true. La asignación reutilizó la referencia existente en lugar de crear una instancia independiente.

Error B: Ignorar el retorno. Llama a createReplacement(pet);sin asignar su resultado. ¿Cambia pet? Explique qué ocurrió con el objeto retornado.

No. El método crea un objeto "Max" y devuelve su referencia, pero el programa ignora ese valor retornado.

Como el objeto nuevo no se guarda en ninguna referencia accesible y no se devuelve a otro método, puede quedar elegible para GC cuando ya no existan referencias alcanzables hacia él.

Error C — Referencia nula. En una copia declara PetProfile missing = null;e intenta missing.registerVisit();. Predice y observa el error, identifica la línea del stack trace y después corrige el acceso.
 El error esperado es NullPointerException, porque missing no apunta a ningún objeto.



*/