package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface  InventarioproductoRepository extends JpaRepository <Inventarioproducto, Integer> {



    @Query(value="SELECT * FROM inventarioproducto where codigogenerado like %?1%",
            countQuery =" SELECT count(*) FROM inventarioproducto where codigogenerado like %?1%",
            nativeQuery = true)
    Page<Inventarioproducto> buscadorInventarioPrincipal(String search, Pageable pageable);


    public Inventarioproducto findByProducto(Producto producto);

}
