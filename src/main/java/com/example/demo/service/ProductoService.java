package com.example.demo.service;

import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.Producto;
import com.example.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductoService implements ProductoServiceApi {
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Page<Producto> getAll(Pageable pageable) {
        return productoRepository.findAll( pageable);
    }

    @Override
    public Page<Producto> getEver(String search, Pageable pageable) {
        return productoRepository.obtenerFiltroProducto(search, pageable);
    }





}
