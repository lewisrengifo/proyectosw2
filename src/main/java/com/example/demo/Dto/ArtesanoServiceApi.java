package com.example.demo.Dto;

import com.example.demo.Entity.Artesano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtesanoServiceApi {

    Page<Artesano> getAll(Pageable pageable);
}
