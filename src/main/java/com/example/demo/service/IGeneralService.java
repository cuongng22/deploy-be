package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T>{
    Page<T> findALl(Pageable pageable);

    Optional<T> findById(Long id);

    T save(T t);

    void deleteById(Long id);

    List<T> findAll();
}
