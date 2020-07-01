package com.example.demo.Controllers;

import com.example.demo.service.ServiceExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/ventas")
public class VentasController2 {
    @Autowired
    ServiceExcel serviceExcel;
    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> exportData() throws Exception{
        ByteArrayInputStream stream = serviceExcel.exportarData();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas.xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
}
