public class MemoryChallenge {

    
    private static void update(PetProfile current) {
        current.registerVisit();

        current = new PetProfile("Nova");
        current.registerVisit();

        System.out.println("Inside: " + current.getName()
                + " (" + current.getVisits() + ")");
    }

    private static PetProfile createReplacement(PetProfile previous) {
        return new PetProfile("Max");
    }

    public static void main(String[] args) {
        PetProfile pet = new PetProfile("Luna");
        PetProfile alias = pet;
        PetProfile testPet = new PetProfile("Luna");
        update(pet);

        System.out.println("Outside: " + pet.getName()
                + " (" + pet.getVisits() + ")");

        PetProfile replacement = createReplacement(pet);

        System.out.println("Same reference: " + (pet == alias));
        System.out.println("Replacement: " + replacement.getName()
                + " (" + replacement.getVisits() + ")");

        

        registerTwoVisits(testPet);

        System.out.println("Visits: " + testPet.getVisits());

        PetProfile newPet = createNewProfile("Sol");

        System.out.println("New profile: " + newPet.getName()
            + " (" + newPet.getVisits() + ")");
    }

    private static void registerTwoVisits(PetProfile pet) {
    pet.registerVisit();
    pet.registerVisit();
        }

    private static PetProfile createNewProfile(String name) {
        return new PetProfile(name);
    }
}

/*
 * ¿Cuántas instrucciones new PetProfile(...)se ejecutarán en total? en total se ejecutarán 3 instrucciones new PetProfile(...), una para "Luna", otra para "Nova" y otra para "Max".
 * ¿Cuántos objetos diferentes se crearán? Se crearán 3 objetos diferentes: uno para "Luna", otro para "Nova" y otro para "Max".
 * ¿ petY aliasapuntan inicialmente al mismo objeto? Sí, pet y alias apuntan inicialmente al mismo objeto "Luna".
 * ¿Qué objeto recibe el primer incremento de visitas? El primer incremento de visitas se realiza en el objeto "Luna" al llamar a current.registerVisit() dentro del método update.
 * ¿Qué objeto recibe el segundo incremento? El segundo incremento de visitas se realiza en el objeto "Nova" al llamar a current.registerVisit() después de crear el nuevo objeto dentro del método update.
 * ¿ createReplacement(pet)Cambia a qué objeto apunta pet? No, createReplacement(pet) no cambia a qué objeto apunta pet. El método createReplacement devuelve un nuevo objeto "Max", pero no modifica la referencia pet en main.
 * ¿Qué cuatro líneas se imprimirán? las líneas que se imprimirán son:
 * Inside: Nova (1)
 * Outside: Luna (1)
 * Same reference: true
 * Replacement: Max (0)
 * Cuando termine update, ¿existe una referencia utilizable desde mainal objeto "Nova"? No, cuando termine update, no existe una referencia utilizable desde main al objeto "Nova" porque la referencia current dentro del método update se reasigna a un nuevo objeto "Nova", y esa referencia no se devuelve ni se almacena en ninguna variable accesible desde main.
 * ¿Qué objetos podrían ser elegibles para GC una vez que se pierdan todas las referencias alcanzables hacia ellos? Los objetos "Luna" y "Nova" podrían ser elegibles para GC una vez que se pierdan todas las referencias alcanzables hacia ellos, ya que no habrá más referencias que apunten a ellos.
 
*Comprueba ambos comportamientos desde main. Explique cuál método cambia   el estado de un objeto y cuál entrega un nuevo valor de referencia para que el llamador pueda asignarlo.

    registerTwoVisits() modifica el estado del objeto recibido, incrementando dos veces su contador de visitas.

    createNewProfile() crea una instancia y devuelve su referencia. El llamador decide guardar ese valor en newPet.
*/