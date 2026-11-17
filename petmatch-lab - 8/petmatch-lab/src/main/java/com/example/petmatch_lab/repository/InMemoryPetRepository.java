package com.example.petmatch_lab.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.petmatch_lab.model.Pet;

@Repository
public class InMemoryPetRepository {

    private final Map<Long, Pet> pets = new LinkedHashMap<>();
    private long nextId = 4L;

    public InMemoryPetRepository() {
        pets.put(1L, new Pet(1L, "Luna", "Perro", 3,
            "Disfruta los paseos y los juegos."));
        pets.put(2L, new Pet(2L, "Milo", "Gato", 2,
            "Prefiere lugares tranquilos."));
        pets.put(3L, new Pet(3L, "Nube", "Conejo", 1,
            "Es curioso y le gusta explorar."));
    }

    public synchronized List<Pet> findAll() {
        return List.copyOf(pets.values());
    }

    public synchronized Optional<Pet> findById(long id) {
        return Optional.ofNullable(pets.get(id));
    }

    public synchronized Pet create(
        String name,
        String species,
        int age,
        String description
    ) {
        long id = nextId++;
        Pet pet = new Pet(id, name, species, age, description);
        pets.put(id, pet);
        return pet;
    }

    public synchronized Optional<Pet> update(
        long id,
        String name,
        String species,
        int age,
        String description
    ) {
        if (!pets.containsKey(id)) {
            return Optional.empty();
        }

        Pet updated = new Pet(id, name, species, age, description);
        pets.put(id, updated);
        return Optional.of(updated);
    }

    public synchronized boolean delete(long id) {
        return pets.remove(id) != null;
    }
}

/* Agrega una cuarta mascota dentro de la lista del repositorio, con identificador 4L. Reinicie la aplicación si es necesario y compruebe que /petsahora muestre 4 sin modificar el controlador ni la plantilla. Explique qué demuestra ese comportamiento acerca de la separación de responsabilidades.La separación de responsabilidades se demuestra porque el repositorio gestiona los datos de las mascotas y el controlador se encarga de la lógica de la aplicación, sin que ninguno dependa directamente del otro. 

Describe qué sucede al pulsar Ver detalle en la tarjeta de Milo, usando correctamente estos términos: th:href, petición HTTP, @PathVariable, PetController, PetService, InMemoryPetRepository, Optional, Model, Thymeleaf y respuesta HTML. al pulsar Ver detalle en la tarjeta de Milo, se genera una petición HTTP GET a la ruta /pets/{id}, donde {id} es el identificador de Milo. El controlador PetController recibe esta petición y utiliza el parámetro @PathVariable para obtener el identificador. Luego, el controlador llama al servicio PetService, que a su vez utiliza el repositorio InMemoryPetRepository para buscar la mascota por su identificador. Si se encuentra la mascota, se devuelve un objeto Optional que contiene la mascota; de lo contrario, se devuelve un Optional vacío. El controlador pasa la mascota al modelo y renderiza la plantilla Thymeleaf para generar la respuesta HTML que muestra los detalles de la mascota.

Debes poder explicar la diferencia entre una lista de objetos y una plantilla repetida, entre th:text y th:each, entre ${...}y @{...}, y entre una ruta desconocida y una mascota inexistente. la diferencia entre una lista de objetos y una plantilla repetida es que la lista de objetos es una colección de datos en memoria, mientras que la plantilla repetida es un fragmento de código HTML que se repite para cada objeto en la lista. th:text se utiliza para mostrar el valor de una variable en la plantilla, mientras que th:each se utiliza para iterar sobre una colección de objetos y generar contenido dinámico. ${...} se utiliza para acceder a variables del modelo en Thymeleaf, mientras que @{...} se utiliza para generar URLs relativas a la aplicación. Una ruta desconocida es una URL que no coincide con ninguna ruta definida en el controlador, mientras que una mascota inexistente es un identificador que no corresponde a ninguna mascota en el repositorio.
*/