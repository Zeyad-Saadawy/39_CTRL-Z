package com.example.service;

import com.example.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Primary
@Service
public abstract class MainService<T> {

    private final MainRepository<T> repository;

    @Autowired
    public MainService(MainRepository<T> repository) {
        this.repository = repository;
    }

    // Common method to get all entities
    public ArrayList<T> findAll() {
        return repository.findAll();
    }

    // Common method to save an entity
    public void save(T entity) {
        repository.save(entity);
    }

    // Common method to save all entities
    public void saveAll(ArrayList<T> entities) {
        repository.saveAll(entities);
    }

    // Common method to override data
    public void overrideData(ArrayList<T> data) {
        repository.overrideData(data);
    }
}