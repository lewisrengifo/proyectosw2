package com.example.demo.Repository;

import com.example.demo.Entity.Tamano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanoRepository extends JpaRepository<Tamano, Integer> {
}

