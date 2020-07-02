package com.example.demo.Repository;


import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Inventariotienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioTiendaRepository extends JpaRepository<Inventariotienda,Integer> {

    @Query(value="select * from inventariotienda where iventariosede_idiventariosede=?1 and tienda_idtienda=?2",nativeQuery=true)
    Inventariotienda ObtenerInventariParacambiarStockParaTienda(int idsede, int idtienda);
}
