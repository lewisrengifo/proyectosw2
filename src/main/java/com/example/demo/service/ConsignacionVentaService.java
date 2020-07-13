package com.example.demo.service;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Ventas;
import com.example.demo.Repository.ConsignacionyventaRepository;
import com.example.demo.Repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ConsignacionVentaService {
    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;

    public Page<Consignacionyventa> listSearch(String search, int page) {
        Pageable pageRequest = PageRequest.of(page, 5);
        return consignacionyventaRepository.buscadorConsignacionesYVentas(search, pageRequest);
    }
}
