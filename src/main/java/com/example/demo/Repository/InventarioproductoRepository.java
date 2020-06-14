package com.example.demo.Repository;

import com.example.demo.Entity.Inventarioproducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  InventarioproductoRepository extends JpaRepository <Inventarioproducto, Integer> {
}
