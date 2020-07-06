package com.example.demo.Repository;


import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Inventariotienda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioTiendaRepository extends JpaRepository<Inventariotienda,Integer> {


    @Query(value="SELECT invt.* FROM inventariotienda  invt , inventarioproducto inve, inventariosede invs ,producto p where p.nombreproducto like %?1% \n" +
            "and p.idproducto=inve.producto_idproducto and invs.inventarioproducto_idinventario= inve.idinventario and invs.idiventariosede = invt.iventariosede_idiventariosede ",
            countQuery ="SELECT count(*) FROM inventariotienda  invt , inventarioproducto inve, inventariosede invs ,producto p where p.nombreproducto like %?1% \n" +
                    "and p.idproducto=inve.producto_idproducto and invs.inventarioproducto_idinventario= inve.idinventario and invs.idiventariosede = invt.iventariosede_idiventariosede",
            nativeQuery = true)
    Page<Inventariotienda> buscadorInventarioTienda(String search, Pageable pageable);
}
