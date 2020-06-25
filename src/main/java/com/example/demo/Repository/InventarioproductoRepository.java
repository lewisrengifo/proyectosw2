package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface  InventarioproductoRepository extends JpaRepository <Inventarioproducto, Integer> {





}
