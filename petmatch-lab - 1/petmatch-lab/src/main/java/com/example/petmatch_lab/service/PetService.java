package com.example.petmatch_lab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.petmatch_lab.model.Pet;
import com.example.petmatch_lab.repository.InMemoryPetRepository;

@Service
public class PetService {

    private final InMemoryPetRepository petRepository;

    public PetService(InMemoryPetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Optional<Pet> findById(long id) {
        return petRepository.findById(id);
    }
}
/*  Elimina temporalmente @Servicede PetService. Intenta iniciar y analiza el error de dependencia que no puede resolverse. el error de dependencia indica que Spring no puede encontrar un bean de tipo PetService para inyectar en el PetController. Esto ocurre porque al eliminar la anotación @Service, Spring ya no reconoce PetService como un componente gestionado y, por lo tanto, no puede crear una instancia de él para satisfacer la dependencia del controlador.


Cambia en el controlador la clave "pets"por "animals", sin cambiar el HTML. Explica por qué deja de funcionar la expresión ${pets.size()}. el cambio de la clave en el controlador significa que el modelo ahora contiene un atributo llamado "animals" en lugar de "pets". Como resultado, la expresión ${pets.size()} en la plantilla HTML ya no encuentra el atributo "pets" y, por lo tanto, no puede calcular su tamaño, lo que provoca que la expresión deje de funcionar. Esto demuestra la importancia de mantener consistencia entre los nombres de los atributos del modelo y las referencias en las vistas.

Coloca PetServicefuera del paquete raíz de la aplicación y observa por qué Spring ya no lo detecta automáticamente. observe que al mover PetService fuera del paquete raíz de la aplicación, Spring no lo detecta automáticamente porque su mecanismo de escaneo de componentes busca clases anotadas con @Service, @Component, @Repository, etc., solo dentro del paquete base y sus subpaquetes. Al estar fuera de este alcance, Spring no registra PetService como un bean, lo que resulta en errores de dependencia al intentar inyectarlo en otros componentes.

Repite un identificador de una mascota. ¿Qué resultado devuelve findByIdcuando existen dos coincidencias? ¿Por qué los identificadores del catálogo deben ser únicos? el método findById devuelve la primera coincidencia que encuentra en la lista de mascotas. Esto significa que si hay dos mascotas con el mismo identificador, solo se devolverá la primera encontrada, y la segunda será ignorada. Los identificadores del catálogo deben ser únicos para garantizar que cada mascota pueda ser identificada de manera inequívoca, evitando confusiones y asegurando la integridad de los datos en el sistema.
*/