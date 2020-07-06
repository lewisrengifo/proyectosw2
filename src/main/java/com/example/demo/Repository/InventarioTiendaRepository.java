package com.example.demo.Repository;


import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Inventariotienda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioTiendaRepository extends JpaRepository<Inventariotienda,Integer> {

    @Query(value="select * from inventariotienda where iventariosede_idiventariosede=?1 and tienda_idtienda=?2",nativeQuery=true)
    Inventariotienda ObtenerInventariParacambiarStockParaTienda(int idsede, int idtienda);



    @Query(value="SELECT it.* FROM  inventariotienda it " +
            "inner join inventariosede invSede on it.iventariosede_idiventariosede=invSede.idiventariosede " +
            "where invSede.sede_idsede=?1",
            countQuery="SELECT Count(it.*) FROM  inventariotienda it " +
            "inner join inventariosede invSede on it.iventariosede_idiventariosede=invSede.idiventariosede " +
            "where invSede.sede_idsede=?1"
            ,nativeQuery=true)
    Page<Inventariotienda> ObtenerInventarioTiendaDeMiSede(int idsede, Pageable pageable);

}
