package com.example.demo.Repository;


import com.example.demo.Dto.UsuarioSedeDto;
import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "UPDATE `dbproyectosw2`.`usuario` SET `enable` = ?1 WHERE (`idUsuario` = ?2);", nativeQuery = true)
    Usuario cambiarenable(String enable, int id);

    public Usuario findByCorreo(String correo);

    @Query(value = "SELECT u.* FROM usuario u, rol r where u.nombre like %?1% and u.rol_idrol= r.idrol or u.apellido like %?1% and u.rol_idrol= r.idrol or u.correo like %?1% and u.rol_idrol= r.idrol or u.sede_idsede =(select idsede from sede where nombre like %?1%) and u.rol_idrol=r.idrol or r.nombre like %?1% and u.rol_idrol= r.idrol",
            countQuery = "SELECT count(*) FROM usuario u, rol r where u.nombre like %?1% and u.rol_idrol= r.idrol or u.apellido like %?1% and u.rol_idrol= r.idrol or u.correo like %?1% and u.rol_idrol= r.idrol or u.sede_idsede =(select idsede from sede where nombre like %?1%) " +
                    "and u.rol_idrol=r.idrol or r.nombre like %?1% and u.rol_idrol= r.idrol", nativeQuery = true)
    Page<Usuario> buscarUsuario(String search, Pageable page);

    //Usuario findByIdusuario(int id);
    @Query(value = "select * from usuario u where u.sede_idsede = (select s.idsede from sede s where s.idsede=?1);", nativeQuery = true)
    List<Usuario> buscarsedeexistente(int idsede);

    @Query(value = "select * from usuario where idUsuario not in (select u.idUsuario from usuario u where u.idUsuario=?1);", nativeQuery = true)
    List<Usuario> buscarmenosmio(int idusuario);

    @Query(value = "select MAX(usuario.idUsuario) as id from usuario", nativeQuery = true)
    int ultimoidinsertado();

    public Usuario findByToken(String tocken);
    @Query(value="SELECT * FROM usuario where enable =1", nativeQuery=true)
    Page<Usuario> usuariosactivos(Pageable page);
    @Query(value="SELECT * FROM usuario where enable =0", nativeQuery=true)
    Page<Usuario> usuariosdesactivados(Pageable page);
    @Query(value = "SELECT * FROM usuario where rol_idrol = 3", nativeQuery = true)
    Page<Usuario> gestoresSede(Pageable page);
    @Query(value = "SELECT * FROM usuario where sede_idsede =?1", nativeQuery = true)
    Usuario usuariodelasede(int it);
    @Query(value = "SELECT u.idUsuario as usuariodelasede FROM usuario u inner join sede s on s.idsede=u.sede_idsede where s.idsede=?1", nativeQuery = true)
    List<UsuarioSedeDto> usuariodelasedeint(int idsede);
    @Query(value="SELECT * from usuario where rol_idrol = 4", nativeQuery= true)
    List<Usuario> usuariosDisponibles();
    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario u SET u.sede_idsede = :idsede, u.rol_idrol = :idrol WHERE (u.idUsuario = :iduser);", nativeQuery = true)
    void actualizarRolSede(@Param("idsede") int sede,@Param("idrol") int rol,@Param("iduser") int idUsuario);
    @Transactional
    @Modifying
    @Query(value= "UPDATE usuario SET sede_idsede = NULL, rol_idrol = '4' WHERE (idUsuario = :idusuario);", nativeQuery = true)
    void actualizarGestorSede(@Param("idusuario") int idusuario);
    @Query(value = "SELECT * FROM usuario where rol_idrol = 2", nativeQuery = true)
    List<Usuario> findGestoresPrincipales();
    //@Query(value = "SELECT * FROM usuario where sede_idsede = ?1", nativeQuery = true)
    //List<Usuario> usuariosdelasede(int sede_idsede);


}

