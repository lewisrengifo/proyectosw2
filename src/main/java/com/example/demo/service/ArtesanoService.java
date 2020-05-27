package com.example.demo.service;

import com.example.demo.Dto.ArtesanoServiceApi;
import com.example.demo.Entity.Artesano;
import com.example.demo.Repository.ArtesanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtesanoService {

    @Autowired
    ArtesanoRepository artesanoRepository;

    public Page<Artesano> listAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber ,5);
        return artesanoRepository.findAll( pageable);
    }


    public Page<Artesano> listSearch(String search, int page){
        Pageable pageRequest = PageRequest.of(page,5);
        return artesanoRepository.buscadorArtesano(search,pageRequest);
    }

}
