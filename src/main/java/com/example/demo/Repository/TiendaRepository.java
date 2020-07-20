package com.example.demo.Repository;

import com.example.demo.Entity.Tienda;
import com.example.demo.Entity.Usuario;
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

    @Query(value = "select * from tienda where nombre not in (select t.nombre from tienda t where t.nombre=?1);", nativeQuery = true)
    List<Tienda> buscarmenosmio(String nombre);
}
