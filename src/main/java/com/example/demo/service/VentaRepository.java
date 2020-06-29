package com.example.demo.service;

import com.example.demo.Entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Ventas,Integer> {
}
