package com.vpactually.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<K, T> {
    List<T> findAll();

    Optional<T> findById(K id);

    T save(T entity);

    T update(T entity);

    boolean deleteById(K id);
}
