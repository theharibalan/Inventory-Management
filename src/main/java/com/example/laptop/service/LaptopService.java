package com.example.laptop.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.laptop.entity.Laptop;
import com.example.laptop.repository.LaptopRepository;

@Service
public class LaptopService {

    @Autowired
    private LaptopRepository laptopRepository;

    public List<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }

    public Optional<Laptop> getLaptopById(Integer laptopId) {  // ✅ Added this method
        return laptopRepository.findById(laptopId);
    }

    public Laptop saveLaptop(Laptop laptop) {
        if (laptop.getLaptopBrand() == null || laptop.getLaptopBrand().isEmpty()
                || laptop.getModelName() == null || laptop.getModelName().isEmpty()
                || laptop.getLaptopTag() == null || laptop.getLaptopTag().isEmpty()) {
            throw new IllegalArgumentException("Laptop details cannot be null or empty");
        }
        return laptopRepository.save(laptop);
    }

    public String deleteLaptopById(Integer id) {
        if (laptopRepository.existsById(id)) {
            laptopRepository.deleteById(id);
            return "The requested id got deleted";
        } else {
            throw new IllegalArgumentException("Laptop with the given id does not exist");
        }
    }
}
