package com.example.demo.Repository;

import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Producto;
import com.example.demo.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  InventarioproductoRepository extends JpaRepository <Inventarioproducto, Integer> {

    public Inventarioproducto findByProducto(Producto producto);

}
