package com.example.demo.Dto;

import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoServiceApi {

    Page<Producto> getAll(Pageable pageable);


}
