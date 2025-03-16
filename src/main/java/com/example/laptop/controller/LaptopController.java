package com.example.laptop.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.laptop.entity.Laptop;
import com.example.laptop.service.LaptopService;

@RestController
@CrossOrigin
@RequestMapping("/laptops")
public class LaptopController {

    @Autowired
    private LaptopService laptopService;

    @GetMapping("/")
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>("API Testing", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Laptop>> listLaptops() {
        List<Laptop> allLaptops = laptopService.getAllLaptops();
        if (allLaptops.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allLaptops, HttpStatus.OK);
    }

    @GetMapping("/{laptopId}")
    public ResponseEntity<Laptop> getLaptopById(@PathVariable Integer laptopId) {
        Optional<Laptop> laptop = laptopService.getLaptopById(laptopId);
        return laptop.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> addLaptop(@RequestBody Laptop laptop) {
        if (laptop.getLaptopBrand() == null || laptop.getLaptopBrand().trim().isEmpty() ||
                laptop.getModelName() == null || laptop.getModelName().trim().isEmpty() ||
                laptop.getLaptopTag() == null || laptop.getLaptopTag().trim().isEmpty()) {

            // Return 400 Bad Request with an error message
            return ResponseEntity.badRequest().body(Map.of("message", "Fields cannot be empty"));
        }

        Laptop savedLaptop = laptopService.saveLaptop(laptop);
        return new ResponseEntity<>(savedLaptop, HttpStatus.CREATED);
    }


    @PutMapping("/{laptopId}")
    public ResponseEntity<Laptop> updateLaptop(@PathVariable Integer laptopId, @RequestBody Laptop updatedLaptop) {
        Optional<Laptop> existingLaptop = laptopService.getLaptopById(laptopId);
        if (existingLaptop.isPresent()) {
            Laptop laptop = existingLaptop.get();
            laptop.setLaptopBrand(updatedLaptop.getLaptopBrand());
            laptop.setModelName(updatedLaptop.getModelName());
            laptop.setLaptopTag(updatedLaptop.getLaptopTag());
            Laptop savedLaptop = laptopService.saveLaptop(laptop);
            return new ResponseEntity<>(savedLaptop, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{laptopId}")
    public ResponseEntity<HttpStatus> deleteLaptop(@PathVariable Integer laptopId) {
        String response = laptopService.deleteLaptopById(laptopId);
        return response.equals("The requested id got deleted") ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
    