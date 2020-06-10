package com.example.demo.Repository;


import com.example.demo.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "UPDATE `dbproyectosw2`.`usuario` SET `enable` = ?1 WHERE (`idUsuario` = ?2);", nativeQuery = true)
    Usuario cambiarenable(String enable, int id);

    public Usuario findByCorreo(String correo);
    @Query(value = "select * from usuario u where u.nombre = ?1 or u.apellido = ?1 or u.dni = ?1 or u.correo=?1 or u.rol_idrol =(select r.idrol from rol r where r.nombre=?1);", nativeQuery = true)
    List<Usuario> buscarUsuario(String search);
    Usuario findByIdusuario(int id);
    @Query(value = "select * from usuario u where u.sede_idrol = (select s.idrol from sede s where s.idrol=?1);", nativeQuery = true)
    List<Usuario> buscarsedeexistente(int idsede);

    @Query(value = "select * from usuario where idUsuario not in (select u.idUsuario from usuario u where u.idUsuario=?1);", nativeQuery = true)
    List<Usuario> buscarmenosmio(int idusuario);
}

