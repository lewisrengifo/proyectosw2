package com.example.demo.service;

import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public Page<Usuario> getAll(int pageNumber) {
        Pageable pageable1 = PageRequest.of(pageNumber, 10);
        return usuarioRepository.findAll(pageable1);
    }

    public Page<Usuario> buscador(String search, Integer pages) {
        Pageable pageable2 = PageRequest.of(pages, 10);
        return usuarioRepository.buscarUsuario(search, pageable2);
    }

    public Page<Usuario> getAllActivos(int pageNumber) {
        Pageable pageable1 = PageRequest.of(pageNumber, 10);
        return usuarioRepository.usuariosactivos(pageable1);
    }

    public Page<Usuario> getAllDesactivados(int pageNumber) {
        Pageable pageable1 = PageRequest.of(pageNumber, 10);
        return usuarioRepository.usuariosdesactivados(pageable1);
    }

    public Page<Usuario> getAllGestSede(int pageNumber) {

        Pageable pageable1 = PageRequest.of(pageNumber, 10);

        return usuarioRepository.gestoresSede(pageable1);
    }
}
