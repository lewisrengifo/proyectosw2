package com.example.demo.service;

import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InventarioPrincipalService {

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;

    public Page<Inventarioproducto> listAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber ,5);
        return inventarioproductoRepository.findAll( pageable);
    }

    public Page<Inventarioproducto> listSearch(String search, int page){
        Pageable pageRequest = PageRequest.of(page,5);
        return inventarioproductoRepository.buscadorInventarioPrincipal(search,pageRequest);
    }

}
