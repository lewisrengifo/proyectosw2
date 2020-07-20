package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Tienda;
import com.example.demo.Entity.Usuario;
import com.example.demo.Entity.Ventas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
    @Query(value = "SELECT * FROM tienda where sede_idsede=?1", nativeQuery = true)
    List<Tienda> listaTiendasPorSede(int id);


    @Query(value = "SELECT * FROM tienda where sede_idsede=?1",
            countQuery = "SELECT * FROM tienda where sede_idsede=?1" , nativeQuery = true)
    Page<Tienda> listaTiendasPorSedePaginado(int id , Pageable pageable);

    @Query(value = "select * from tienda where idtienda not in (select t.idtienda from tienda t where t.idtienda=?1);", nativeQuery = true)
    List<Tienda> buscarmenosmio(int id);

    @Query(value="select * from proyectobasesw2.ventas where tienda_idtienda = ?1 limit 1;",nativeQuery = true)
    Ventas verificaidTiendaenVentas(int id);


    @Query(value="SELECT * from tienda where tienda.nombre like (?1) and tienda.sede_idsede=?2 limit 1;" , nativeQuery = true)
    Tienda verificaidTienda(String nombre, int id);
}
