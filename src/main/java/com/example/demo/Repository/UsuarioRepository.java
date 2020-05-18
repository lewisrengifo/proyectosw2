package com.example.demo.Repository;


import com.example.demo.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "UPDATE `dbproyectosw2`.`usuario` SET `enable` = ?1 WHERE (`idUsuario` = ?2);", nativeQuery = true)
    Usuario cambiarenable(String enable, int id);

    public Usuario findByCorreo(String correo);
}

