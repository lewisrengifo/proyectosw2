package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Inventariotienda;
import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InventarioSedeRepository extends JpaRepository<Inventariosede,Integer> {

    @Query(value="SELECT * FROM inventariosede invs, sede se where se.nombre=?1 and invs.sede_idsede=se.idsede",
            countQuery = "SELECT * FROM inventariosede invs, sede se where se.nombre=?1 and invs.sede_idsede=se.idsede", nativeQuery = true)
    Page<Inventariosede> obtenerInvDeMiSede(String sesionSede, Pageable pageable);


    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariosede SET estado = :estado WHERE (idiventariosede = :idinventariosede);", nativeQuery = true)
    void actualizarEstado(@Param("estado") String estado, @Param("idinventariosede")int idinventariosede);
    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariosede SET observaciones = :observaciones WHERE (idiventariosede = :idinventariosede);", nativeQuery = true)
    void actualizarObservaciones(@Param("observaciones") String observaciones, @Param("idinventariosede")int idinventariosede);


    @Query(value="SELECT * FROM inventariosede where sede_idsede=?1 and estado ='recibido'",nativeQuery=true)
    List<Inventariosede> listarInventarioPorSede(int idSede);

    @Query(value="SELECT * FROM inventariosede invs where invs.sede_idsede = (select idsede from sede where nombre = ?1)"
         , nativeQuery = true)
    List<Inventariosede> obtenerInvDeMiSedeNormal(String sesionSede);

    @Query(value="SELECT * FROM inventariosede where inventarioproducto_idinventario=?1 and sede_idsede=?2",nativeQuery=true)
    Inventariosede ObtenerInventariParacambiarStockParaSede(int invProducto, int idsede);



    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariosede SET stock = :cantidad WHERE (idiventariosede = :idinventariosede);", nativeQuery = true)
    void actualizarStockSede(@Param("cantidad") int cantidadNew, @Param("idinventariosede")int idinventariosede);

    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariotienda SET stocktienda = :cantidad WHERE (idiventariotienda = :idiventariotienda);", nativeQuery = true)
    void actualizarStockTienda(@Param("cantidad") int cantidadNew, @Param("idiventariotienda")int idiventariotienda);

    @Query(value="SELECT * FROM inventariosede where sede_idsede=?1 and inventarioproducto_idinventario=?2",nativeQuery=true)
    Inventariosede obtenerStockSedePrincipal(int idSedePrincipal, int idinventarioProducto);

    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariosede SET stock = :stock WHERE (idiventariosede = :idinventariosede);", nativeQuery = true)
    void actualizarStockSedeXVenta(@Param("stock") int stock, @Param("idinventariosede")int idinventariosede);




}
