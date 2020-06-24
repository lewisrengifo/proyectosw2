package com.example.demo.Repository;

import com.example.demo.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    @Query(value="SELECT * FROM rol where nombre='Gestor principal'", nativeQuery=true)
    List<Rol> rolgestorprincipal();
    @Query(value="SELECT * FROM rol where nombre='Gestor sede'", nativeQuery=true)
    List<Rol> rolgestorsede();
}
