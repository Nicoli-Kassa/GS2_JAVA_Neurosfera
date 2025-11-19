package com.neurosfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {

    // Buscar distritos por área
    List<Distrito> findByAreaDistrito(String areaDistrito);

    // Buscar distritos ativos
    List<Distrito> findByStDistritoTrue();

    // Verificar se já existe distrito com este nome
    boolean existsByNmDistrito(String nmDistrito);
}