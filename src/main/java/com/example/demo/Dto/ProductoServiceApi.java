package com.example.demo.Dto;

import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ProductoServiceApi {

    Page<Producto> getAll(Pageable pageable);
    Page<Producto> getEver(String search ,Pageable pageable);

    Page<Inventariosede> getEverInvs(String search, Pageable pageable) ;





}
