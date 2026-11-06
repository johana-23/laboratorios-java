package com.example.petmatch_lab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.petmatch_lab.service.PetService;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pets", petService.findAll());
        return "pets/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") long id, Model model) {
    var pet = petService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Pet not found"
        ));

    model.addAttribute("pet", pet);
    return "pets/detail";
    }
}
/* ¿Qué objeto construye directamente el PetControllercon new? constructor del PetController no construye ningún objeto directamente con new. En su lugar, recibe una instancia de PetService a través de la inyección de dependencias proporcionada por Spring.
¿Quién crea el PetServicecuando inicia la aplicación?
El PetService es creado por el contenedor de Spring cuando inicia la aplicación. Spring detecta la anotación @Service en la clase PetService y la registra como un bean en el contexto de la aplicación, gestionando su ciclo de vida y permitiendo que sea inyectado en otras clases, como el PetController.
¿En qué clase se encuentran los tres new Pet(...)? en la clase InMemoryPetRepository. Esta clase crea una lista de objetos Pet utilizando el constructor de la clase Pet para inicializar cada instancia con sus respectivos atributos (id, nombre, especie, edad y descripción).
¿Cuál es la clave del atributo agregado al Model? "pets"
¿Qué método del servicio se invoca al visitar /pets? findAll() */