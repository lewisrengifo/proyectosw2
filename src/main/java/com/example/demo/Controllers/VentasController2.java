package com.example.demo.Controllers;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.service.ServiceExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventasexcel")
public class VentasController2 {
    @Autowired
    ServiceExcel serviceExcel;

    @GetMapping("")
    public String paginaReportes(){
        return "Reportes/principal";
    }

    @PostMapping("/mes")
    public ResponseEntity<InputStreamResource> exportData(@RequestParam("mes")String mes) throws Exception{
        List<ReporteMensualoAnualMosqoyDto> lista= new ArrayList<>();
        ByteArrayInputStream stream = serviceExcel.exportarData(mes,lista,mes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas.xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }


}
