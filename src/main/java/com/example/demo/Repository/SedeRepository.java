package com.example.demo.Repository;


import com.example.demo.Entity.Rol;

import com.example.demo.Entity.Inventariosede;

import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;


@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    //Sede findByIdrol(int idsede);
    @Query(value="SELECT * FROM sede where nombre like %?1%", nativeQuery= true)
    Page<Sede> buscarSede(String search, Pageable page);

    Sede  findTopByOrderByIdsedeDesc();


    @Query(value="SELECT * FROM sede where nombre=?1", nativeQuery=true)
    Sede sedePornombre(String nom);

    @Query(value = "SELECT * FROM sede where idsede not in (select idsede from sede where idsede=?1)",nativeQuery = true)
    List<Sede> listaSedeSinPrincipal(int id);





}
