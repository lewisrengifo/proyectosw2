package com.example.demo.Repository;


import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "UPDATE `dbproyectosw2`.`usuario` SET `enable` = ?1 WHERE (`idUsuario` = ?2);", nativeQuery = true)
    Usuario cambiarenable(String enable, int id);

    public Usuario findByCorreo(String correo);
    @Query(value = "SELECT * FROM usuario u where u.nombre like %?1% or u.apellido like %?1% or u.correo like %?1% or u.sede_idsede =(select idsede from sede where nombre like %?1%) or u.rol_idrol=(select idrol from rol where nombre like %?1%)", countQuery= "SELECT count(*) FROM usuario u where u.nombre like %?1% or u.apellido like %?1% or u.correo like %?1% or u.sede_idsede =(select idsede from sede where nombre like %?1%) or u.rol_idrol=(select idrol from rol where nombre like %?1%)" ,nativeQuery = true)
    Page<Usuario> buscarUsuario(String search, Pageable page);
    //Usuario findByIdusuario(int id);
    @Query(value = "select * from usuario u where u.sede_idsede = (select s.idsede from sede s where s.idsede=?1);", nativeQuery = true)
    List<Usuario> buscarsedeexistente(int idsede);

    @Query(value = "select * from usuario where idUsuario not in (select u.idUsuario from usuario u where u.idUsuario=?1);", nativeQuery = true)
    List<Usuario> buscarmenosmio(int idusuario);

    @Query(value = "select MAX(usuario.idUsuario) as id from usuario", nativeQuery = true)
    int ultimoidinsertado();

    public Usuario findByToken(String tocken);

}

