package com.example.service;

import com.example.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public abstract class MainService<T> {

    protected final MainRepository<T> repository; // Use protected for subclass access

    public MainService(MainRepository<T> repository) {
        this.repository = repository;
    }

    public ArrayList<T> findAll() {
        return repository.findAll();
    }

    public void save(T entity) {
        repository.save(entity);
    }

    public void saveAll(ArrayList<T> entities) {
        repository.saveAll(entities);
    }

    public void overrideData(ArrayList<T> data) {
        repository.overrideData(data);
    }
}