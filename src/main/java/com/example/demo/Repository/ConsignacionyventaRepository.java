package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsignacionyventaRepository extends JpaRepository <Consignacionyventa, Integer> {

    @Query(value="SELECT MAX(idconsignacion) as id FROM consignacionyventa",nativeQuery=true)
    int ultimoConsiyVentaIngresado();

    Consignacionyventa findTopByOrderByIdconsignacionDesc();
}
