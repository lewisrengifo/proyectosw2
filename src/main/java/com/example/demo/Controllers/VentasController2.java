package com.example.demo.Controllers;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Comunidad;
import com.example.demo.Entity.Sede;
import com.example.demo.Repository.*;
import com.example.demo.service.ServiceExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    VentaRepository1 ventaRepository1;

    @GetMapping("")
    public String paginaReportes(){
        return "Reportes/principal";
    }

    @PostMapping("/ano")
    public ResponseEntity<InputStreamResource> exportDataAnual(@RequestParam("ano")String ano) throws Exception{
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteAnualMosqoy(ano);
        ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,ano);//cambiar 2variable ano por un string que sea igual a lo correspondiente
        HttpHeaders headers = new HttpHeaders();
        String archivo = "hola"; //titulo del excel, no del sheet
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/anomes")
    public ResponseEntity<InputStreamResource> exportDataAnualyMensual(@RequestParam("anomes")String anomes,@RequestParam("mes")String mes) throws Exception{
        String mes1="";
        switch (mes){
            case "Enero":
                mes1="01";
                break;

            case "Febrero":
                mes1="02";
                break;

            case "Marzo":
                mes1="03";
                break;

            case "Abril":
                mes1="04";
                break;

            case "Mayo":
                mes="05";
                break;

            case "Junio":
                mes1="06";
                break;

            case "Julio":
                mes1="07";
                break;

            case "Agosto":
                mes1="08";
                break;

            case "Septiembre":
                mes1="09";
                break;

            case "Octubre":
                mes1="10";
                break;

            case "Noviembre":
                mes1="11";
                break;

            case "Diciembre":
                mes1="12";
                break;
            default: mes1="#";
        }
        String dato=anomes+"-"+mes1;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteAnualMosqoy(dato);
        String aux="Mes "+mes;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anomes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas.xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/trimestre")
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestral(@RequestParam("trimestre")String trimestre,@RequestParam("anotri")String anotri) throws Exception{
        String mes1="";
        String mes2="";
        String mes3="";
        switch (trimestre){
            case "Primero":
                mes1="01";
                mes2="02";
                mes3="03";
                break;

            case "Segundo":
                mes1="04";
                mes2="05";
                mes3="06";
                break;

            case "Tercero":
                mes1="07";
                mes2="08";
                mes3="09";
                break;

            case "Cuarto":
                mes1="10";
                mes2="11";
                mes3="12";
                break;
            default: mes1="#";
        }
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestralAnualMosqoy(mes1,mes2,mes3,anotri);
        String aux="Trimestre "+ trimestre ;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas.xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //--------------REPORTES POR SEDE--------------------------------------------
    @Autowired
    SedeRepository sedeRepository;

    @GetMapping("/sede")
    public String paginaReportesSede(Model model){
        List<Sede> listaSede = sedeRepository.findAll();
        model.addAttribute("listaSede", listaSede);
        return "Reportes/sede";
    }

    @PostMapping("/ano/sede")
    public ResponseEntity<InputStreamResource> exportDataAnualSede(@RequestParam("ano")String ano, Sede sede) throws Exception{

        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteMensualAnualSede(ano, sede.getIdsede());
        ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,ano);//cambiar 2variable ano por un string que sea igual a lo correspondiente
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte anual del año"+ " "+ ano +" de la sede" + " " + sede.getNombre(); //titulo del excel, no del sheet
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/anomes/sede")
    public ResponseEntity<InputStreamResource> exportDataAnualyMensualSede(@RequestParam("anomes")String anomes,@RequestParam("mes")String mes, Sede sede) throws Exception{
        String mes1="";
        switch (mes){
            case "Enero":
                mes1="01";
                break;

            case "Febrero":
                mes1="02";
                break;

            case "Marzo":
                mes1="03";
                break;

            case "Abril":
                mes1="04";
                break;

            case "Mayo":
                mes="05";
                break;

            case "Junio":
                mes1="06";
                break;

            case "Julio":
                mes1="07";
                break;

            case "Agosto":
                mes1="08";
                break;

            case "Septiembre":
                mes1="09";
                break;

            case "Octubre":
                mes1="10";
                break;

            case "Noviembre":
                mes1="11";
                break;

            case "Diciembre":
                mes1="12";
                break;
            default: mes1="#";
        }
        String dato=anomes+"-"+mes1;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteMensualAnualSede(dato, sede.getIdsede());
        String aux="Mes "+mes;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anomes);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte mensual de la sede" + " " + sede.getNombre(); //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/trimestre/sede")
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestralSede(@RequestParam("trimestre")String trimestre,@RequestParam("anotri")String anotri, Sede sede) throws Exception{
        String mes1="";
        String mes2="";
        String mes3="";
        switch (trimestre){
            case "Primero":
                mes1="01";
                mes2="02";
                mes3="03";
                break;

            case "Segundo":
                mes1="04";
                mes2="05";
                mes3="06";
                break;

            case "Tercero":
                mes1="07";
                mes2="08";
                mes3="09";
                break;

            case "Cuarto":
                mes1="10";
                mes2="11";
                mes3="12";
                break;
            default: mes1="#";
        }
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestralAnualMosqoy(mes1,mes2,mes3,anotri);
        String aux="Trimestre "+ trimestre ;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas.xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //---------REPORTES POR ARTICULOS

    @Autowired
    ProductoRepository productoRepository;
    @GetMapping("/articulos")
    public String paginaReportesArticulo(Model model){
        model.addAttribute("listaProducto", productoRepository.findAll());
        return "Reportes/producto";
    }


    //REPORTES POR COMUNIDAD
    @Autowired
    ComunidadRepository comunidadRepository;
    @GetMapping("/comunidad")
    public String paginaReportesComunidad(Model model){
        List<Comunidad> listaComunidad = comunidadRepository.findAll();
        model.addAttribute("listaComunidad", listaComunidad);
        return "Reportes/comunidad";
    }
    @PostMapping("/ano/comunidad")
    public ResponseEntity<InputStreamResource> exportDataAnualComunidad(@RequestParam("ano")String ano, Comunidad comunidad) throws Exception{
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteComunidad(ano, comunidad.getIdcomunidad());
        ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,ano);//cambiar 2variable ano por un string que sea igual a lo correspondiente
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte anual del año " + ano +" de la comunidad" + " " + comunidad.getNombrecomunidad(); //titulo del excel, no del sheet
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/anomes/comunidad")
    public ResponseEntity<InputStreamResource> exportDataAnualyMensualComunidad(@RequestParam("anomes")String anomes,@RequestParam("mes")String mes, Sede sede) throws Exception{
        String mes1="";
        switch (mes){
            case "Enero":
                mes1="01";
                break;

            case "Febrero":
                mes1="02";
                break;

            case "Marzo":
                mes1="03";
                break;

            case "Abril":
                mes1="04";
                break;

            case "Mayo":
                mes="05";
                break;

            case "Junio":
                mes1="06";
                break;

            case "Julio":
                mes1="07";
                break;

            case "Agosto":
                mes1="08";
                break;

            case "Septiembre":
                mes1="09";
                break;

            case "Octubre":
                mes1="10";
                break;

            case "Noviembre":
                mes1="11";
                break;

            case "Diciembre":
                mes1="12";
                break;
            default: mes1="#";
        }
        String dato=anomes+"-"+mes1;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteMensualAnualSede(dato, sede.getIdsede());
        String aux="Mes "+mes;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anomes);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte mensual de la comunidad" + " " + sede.getNombre(); //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/trimestre/comunidad")
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestralComunidad(@RequestParam("trimestre")String trimestre,@RequestParam("anotri")String anotri, Sede sede) throws Exception{
        String mes1="";
        String mes2="";
        String mes3="";
        switch (trimestre){
            case "Primero":
                mes1="01";
                mes2="02";
                mes3="03";
                break;

            case "Segundo":
                mes1="04";
                mes2="05";
                mes3="06";
                break;

            case "Tercero":
                mes1="07";
                mes2="08";
                mes3="09";
                break;

            case "Cuarto":
                mes1="10";
                mes2="11";
                mes3="12";
                break;
            default: mes1="#";
        }
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestralAnualMosqoy(mes1,mes2,mes3,anotri);
        String aux="Trimestre "+ trimestre ;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas.xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //REPORTES DE CLIENTE
    @Autowired
    VentasRepository ventasRepository;
    @GetMapping("/cliente")
    public String paginaReportesCliente(Model model){
        model.addAttribute("listaCliente", ventasRepository.findAll());
        return "Reportes/cliente";
    }

}
