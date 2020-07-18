package com.example.demo.Repository;

import com.example.demo.Entity.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Integer> {
    @Query(value = "select * from notificaciones where usuario_idUsuario =?1", nativeQuery = true)
    Notificaciones findByUserId(int id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE `proyectobasesw2`.`notificaciones` SET `fecha` = :mes, `flag` = '1' WHERE (`usuario_idUsuario` = :idusuario);", nativeQuery = true)
    void actualizarFlagFecha(@Param("idusuario") int idusuario, @Param("mes")Date mes);
    @Transactional
    @Modifying
    @Query(value = "UPDATE `proyectobasesw2`.`notificaciones` SET `flag` = '0' WHERE (`usuario_idUsuario` = :idusuario);", nativeQuery = true)
    void actualizarFlagFalse(@Param("idusuario") int idusuario);
}
