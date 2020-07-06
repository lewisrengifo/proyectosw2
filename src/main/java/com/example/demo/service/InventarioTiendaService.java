package com.example.demo.service;

import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Inventariotienda;
import com.example.demo.Repository.InventarioTiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InventarioTiendaService {

    @Autowired
    InventarioTiendaRepository inventarioTiendaRepository;

    public Page<Inventariotienda> listaInventarioTiendaMiSede(int idsede, int page){
        Pageable pageRequest = PageRequest.of(page,5);
        return inventarioTiendaRepository.ObtenerInventarioTiendaDeMiSede(idsede,pageRequest);
    }
}
