package com.workintech.rest.controller;

import com.workintech.rest.entity.Animal;
import com.workintech.rest.mapping.AnimalResponse;
import com.workintech.rest.validation.AnimalValidation;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workintech/animal")
public class AnimalController {

    @Value("${student.firstName}")
    private String firstName;
    @Value("${student.lastName}")
    private String lastName;

    private Map<Integer, Animal> animalMap;

    // post-construct
    @PostConstruct
    public void init() {
        animalMap = new HashMap<>();
    }

    // public AnimalController() {
    //     animalMap = new HashMap<>()
    // }

    @GetMapping("/welcome")
    public String welcome() {
        return firstName + " - " + lastName + " says hi!";
    }

    @GetMapping("/")
    public List<Animal> get() {
        return animalMap.values().stream().toList();
    }

    @GetMapping("/{id}")
    public AnimalResponse get(@PathVariable int id) {
        if (!AnimalValidation.isIdValid(id)) {
            return new AnimalResponse(null, "ID is not valid", 400);
        }
        if (!AnimalValidation.isMapContainKey(animalMap, id)) {
            return new AnimalResponse(null, "Animal does not exist", 400);
        }
        return new AnimalResponse(animalMap.get(id), "Success", 200);
    }

    @PostMapping("/")
    public AnimalResponse save(@RequestBody Animal animal) {
        if (AnimalValidation.isMapContainKey(animalMap, animal.getId())) {
            return new AnimalResponse(null, "Animal already exists", 400);
        }
        if (!AnimalValidation.isAnimalCredentialsValid(animal)) {
            return new AnimalResponse(null, "Animal credentials are not valid", 400);
        }
        animalMap.put(animal.getId(), animal);
        return new AnimalResponse(animalMap.get(animal.getId()), "Success", 200);
    }

    @PutMapping("/{id}")
    public AnimalResponse update(@PathVariable int id, @RequestBody Animal animal) {
        if (!AnimalValidation.isMapContainKey(animalMap, animal.getId())) {
            return new AnimalResponse(null, "Animal does not exist", 400);
        }
        if (!AnimalValidation.isAnimalCredentialsValid(animal)) {
            return new AnimalResponse(null, "Animal credentials are not valid", 400);
        }
        animalMap.put(id, new Animal(id, animal.getName()));
        return new AnimalResponse(animalMap.get(id), "Success", 200);
    }

    @DeleteMapping("/{id}")
    public AnimalResponse delete(@PathVariable int id) {
        if (!AnimalValidation.isMapContainKey(animalMap, id)) {
            return new AnimalResponse(null, "Animal does not exist", 400);
        }
        Animal foundAnimal = animalMap.get(id);
        animalMap.remove(id);
        return new AnimalResponse(foundAnimal, "Success", 200);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Animal Controller has been destroyed");
    }
}
