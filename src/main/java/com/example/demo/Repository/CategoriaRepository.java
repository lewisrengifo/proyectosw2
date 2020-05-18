package com.example.demo.Repository;

import com.example.demo.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    @Query(value="SELECT nombrecategoria from categoria where nombrecategoria= ?1",nativeQuery=true)
     String verificarNombre(String nombreCategoria);



}
