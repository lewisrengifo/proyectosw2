package com.example.demo.Repository;

import com.example.demo.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {

    @Query(value="SELECT nombrecategoria from categoria where nombrecategoria= ?1",nativeQuery=true)
     String verificarNombre(String nombreCategoria);

    @Query(value = "SELECT * FROM categoria where idcategoria not in (Select c.idcategoria from categoria c where c.idcategoria=?1);"
            , nativeQuery = true)
    List<Categoria> mio(int id);


}
