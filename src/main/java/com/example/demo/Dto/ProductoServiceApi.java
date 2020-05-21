package com.example.demo.Dto;

import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoServiceApi {

    Page<Producto> getAll(Pageable pageable);
    Page<Producto> getEver1(String search ,Pageable pageable);
    Page<Producto> getEver2(String search ,Pageable pageable);
    Page<Producto> getEver3(String search ,Pageable pageable);



}
