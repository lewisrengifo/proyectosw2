package com.example.demo.service;

import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Producto;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProductoService implements ProductoServiceApi {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;

    @Override
    public Page<Producto> getAll(Pageable pageable) {
        return productoRepository.findAll( pageable);
    }

    @Override
    public Page<Producto> getEver(String search, Pageable pageable) {
        return productoRepository.obtenerFiltroProducto(search, pageable);
    }

    @Override
        public Page<Inventariosede> getEverInvs(String search, Pageable pageable) {
            return inventarioSedeRepository.obtenerInvDeMiSede(search, pageable);

    }











}
