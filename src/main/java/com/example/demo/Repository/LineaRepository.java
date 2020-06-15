package com.example.demo.Repository;

import com.example.demo.Entity.Linea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaRepository  extends JpaRepository <Linea, Integer> {
}
