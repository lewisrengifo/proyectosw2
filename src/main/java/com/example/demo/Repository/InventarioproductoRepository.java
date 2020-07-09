package com.example.demo.Repository;

import antlr.collections.List;
import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;

import com.example.demo.Entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.Entity.Producto;
import com.example.demo.Entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface InventarioproductoRepository extends JpaRepository <Inventarioproducto, Integer> {



    @Query(value="SELECT * FROM inventarioproducto where codigogenerado like %?1%",
            countQuery =" SELECT count(*) FROM inventarioproducto where codigogenerado like %?1%",
            nativeQuery = true)
    Page<Inventarioproducto> buscadorInventarioPrincipal(String search, Pageable pageable);


    @Transactional
    @Modifying
    @Query(value= "UPDATE inventarioproducto SET cantidad = :cantidad WHERE (idinventario = :idinventario);", nativeQuery = true)
    void ActualizarCantidadInventarioPrincipal(@Param("cantidad") int cantidad, @Param("idinventario") int idinventario);

    //public Inventarioproducto findByProducto(Producto producto);







}
