package com.example.demo.service;


import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Ventas;
import com.example.demo.Repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VentasService {
    @Autowired
    VentaRepository ventaRepository;

    public Page<Ventas> listAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber ,5);
        return ventaRepository.findAll( pageable);
    }

    public Page<Ventas> listSearch(String search, int page){
        Pageable pageRequest = PageRequest.of(page,5);
        return ventaRepository.buscadorVentas(search,pageRequest);
    }




}
