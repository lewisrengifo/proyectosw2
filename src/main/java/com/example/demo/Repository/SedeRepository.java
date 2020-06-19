package com.example.demo.Repository;

import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    //Sede findByIdrol(int idsede);
    @Query(value="SELECT * FROM sede where nombre like %?1%", nativeQuery= true)
    Page<Sede> buscarSede(String search, Pageable page);
}
