package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface InventarioSedeRepository extends JpaRepository<Inventariosede,Integer> {

    @Query(value="SELECT * FROM inventariosede invs where invs.sede_idsede = (select idsede from sede where nombre = ?1)",
            countQuery = "SELECT count(*) FROM inventariosede invs where invs.sede_idsede = (select idsede from sede where nombre = ?1);", nativeQuery = true)
    Page<Inventariosede> obtenerInvDeMiSede(String sesionSede, Pageable pageable);
}
