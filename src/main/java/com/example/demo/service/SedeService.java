package com.example.demo.service;

import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SedeService {
    @Autowired
    SedeRepository sedeRepository;
    public Page<Sede> getAll(int pageNumber){
        Pageable pageable1 = PageRequest.of(pageNumber, 5);
        return sedeRepository.findAll(pageable1);
    }
    public Page<Sede> buscador(String search, Integer pages){
        Pageable pageable2 = PageRequest.of(pages, 5);
        return sedeRepository.buscarSede(search, pageable2);
    }

}
