package com.example.demo.Dto;

import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioServiceApi {
    Page<Usuario> getAll(Pageable pageable);
}
