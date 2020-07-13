package com.example.demo.Controllers;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Comunidad;
import com.example.demo.Entity.Producto;
import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Ventas;

import com.example.demo.Repository.*;

import com.example.demo.service.ServiceExcel;
import com.sun.istack.Nullable;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ventasexcel")
public class VentasController2 {
    @Autowired
    ServiceExcel serviceExcel;

    @Autowired
    VentaRepository1 ventaRepository1;

    @Autowired
    SedeRepository sedeRepository;

    @Autowired
    ComunidadRepository comunidadRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    VentasRepository ventasRepository;


    @GetMapping("")
    public String paginaReportes(Model model){
        model.addAttribute("listaComunidad",comunidadRepository.findAll());
        model.addAttribute("listaComunidad1",comunidadRepository.findAll());
        model.addAttribute("listaSede",sedeRepository.findAll());
        model.addAttribute("listaSede1",sedeRepository.findAll());
        model.addAttribute("listaProducto",productoRepository.findAll());
        model.addAttribute("listaProducto1", productoRepository.findAll());
        model.addAttribute("listaVentas", ventasRepository.findAll());
        model.addAttribute("listaVentas1", ventasRepository.findAll());
        return "Reportes/principal";
    }


    /*@PostMapping("/ano")
    public String reporteAno(@RequestParam("ano")String ano, RedirectAttributes att) {
        try{
            int a = Integer.valueOf(ano);
            ResponseEntity<InputStreamResource> inputStreamResourceResponseEntity = null;

            att.addFlashAttribute("msg", "Se descargo correctamente el documento");
            return  "redirect:/ventasexcel/ano/excel?ano="+ano;
        }catch (NumberFormatException e){
            att.addFlashAttribute("msg", "Tiene que ingresar un numero");
            return  "redirect:/ventasexcel";
        } catch (Exception e) {
            e.printStackTrace();
            att.addFlashAttribute("msg", "Ocurrio un error");
            return"redirect:/ventasexcel";
        }
    }*/
    @PostMapping("/ano")
    public ResponseEntity<InputStreamResource> exportDataAnual(@RequestParam("ano")String ano) throws Exception{

            List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteAnualMosqoy(ano);
            String tipo="Año";
            ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,tipo);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition","attachment; filename=Ventas"+ano+".xls");
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
                mes1="05";
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
        ByteArrayInputStream stream = serviceExcel.exportarData(anomes,lista,aux);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition","attachment; filename=Ventas"+mes+anomes+".xls");

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

        String mmes1 = anotri +"-"+ mes1;
        String mmes2 = anotri +"-"+ mes2;
        String mmes3 = anotri +"-"+ mes3;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestralAnualMosqoy(mmes1,mmes2,mmes3);
        String aux="Trimestre " +trimestre;
        ByteArrayInputStream stream = serviceExcel.exportarData(anotri,lista,aux);

        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte trimestral de Mosqoy:" + trimestre; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+archivo+".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //--------------REPORTES POR SEDE--------------------------------------------


    @GetMapping("/sede")
    public String paginaReportesSede(Model model){
        List<Sede> listaSede = sedeRepository.findAll();
        model.addAttribute("listaSede", listaSede);
        return "Reportes/sede";
    }

    public String pruebaFiltro (@RequestParam("ano")String ano, Sede sede, Model model){

        //lógica para validar
        try{
            Integer anoint = Integer.parseInt(ano);
            Optional<Sede> sedeporId = sedeRepository.findById(sede.getIdsede());
            Date hoy = new Date();

            if (anoint != 0){

            }
        }catch (NumberFormatException e){
            model.addAttribute("mensaje", "Debe ingresar el año adecuado");
            return "reportes/principal";
        }



        return "reportes/principal";
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
                mes1="05";
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
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestralSede(@RequestParam("trimestre")String trimestre,@RequestParam("anotri")String anotri,@RequestParam("sede1") String sede) throws Exception{
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
        String mmes1 = anotri +"-"+ mes1;
        String mmes2 = anotri +"-"+ mes2;
        String mmes3 = anotri +"-"+ mes3;

        Sede sede1=sedeRepository.sedePornombre(sede);
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestralSede(mmes1,mmes2,mmes3,sede1.getIdsede());
        String aux="Trimestre "+ trimestre;

        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte trimestral de la sede" + " " + sede1.getNombre(); //titulo del excel
        headers.add("Content-Disposition","attachment; filename=" +archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //-----FIN DE SEDE

    //---------REPORTES POR ARTICULOS(productos)



    @PostMapping("/ano/producto")
    public ResponseEntity<InputStreamResource> exportDataAnualProducto(@RequestParam("anopro") String ano, @RequestParam("producto1") String producto) throws Exception{

        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteProducto(ano, producto);
        ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,ano);//cambiar 2variable ano por un string que sea igual a lo correspondiente
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte anual del año " + ano +" del producto:" + " " + producto; //titulo del excel, no del sheet
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/anomes/producto")
    public ResponseEntity<InputStreamResource> exportDataAnualyMensualProducto(@RequestParam("anomespro")String anomes,@RequestParam("mespro")String mes,@RequestParam("producto2") String  producto) throws Exception{
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
                mes1="05";
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

        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteProducto(dato, producto);
        String aux="Mes "+mes;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anomes);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte mensual del producto:" + " " + producto; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/trimestre/producto")
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestralProducto(@RequestParam("trimestrepro")String trimestre,@RequestParam("anotripro")String anotri, @RequestParam("producto3") String producto) throws Exception{
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

        String mmes1 = anotri +"-"+ mes1;
        String mmes2 = anotri +"-"+ mes2;
        String mmes3 = anotri +"-"+ mes3;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestreProducto(mmes1,mmes2,mmes3, producto);
        String aux="Trimestre "+ trimestre ;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte trimestral del producto:" + " " + producto; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo+".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //----------FIN DE PRODUCTO

    //REPORTES POR COMUNIDAD


    @PostMapping("/ano/comunidad")
    public ResponseEntity<InputStreamResource> exportDataAnualComunidad(@RequestParam("anocomunidad")String ano, @RequestParam("com1") String comunidad) throws Exception{
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteComunidad(ano, comunidad);
        ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,ano);//cambiar 2variable ano por un string que sea igual a lo correspondiente
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte anual del año " + ano +" de la comunidad:" + " " + comunidad; //titulo del excel, no del sheet
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/anomes/comunidad")
    public ResponseEntity<InputStreamResource> exportDataAnualyMensualComunidad(@RequestParam("anomescomunidad")String anomes,@RequestParam("mescomunidad")String mes, @RequestParam("com2") String comunidad) throws Exception{
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
                mes1="05";
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
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteComunidad(dato, comunidad);
        String aux="Mes "+mes;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anomes);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte mensual de la comunidad:" + " " + comunidad; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/trimestre/comunidad")
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestralComunidad(@RequestParam("trimestrecomunidad")String trimestre,@RequestParam("anotricomunidad")String anotri, @RequestParam("com3") String comunidad) throws Exception{
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
        String mmes1 = anotri +"-"+ mes1;
        String mmes2 = anotri +"-"+ mes2;
        String mmes3 = anotri +"-"+ mes3;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestreComunidad(mmes1,mmes2,mmes3, comunidad);
        String aux="Trimestre "+ trimestre ;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte trimestral de la comunidad:" + " " + comunidad; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo+".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    //----------FIN COMUNIDAD

    //REPORTES DE CLIENTE

    @PostMapping("/ano/cliente")
    public ResponseEntity<InputStreamResource> exportDataAnualCliente(@RequestParam("anocli")String ano, @RequestParam("cliente1") String ventas) throws Exception{

        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteCliente(ano, ventas);
        ByteArrayInputStream stream = serviceExcel.exportarData(ano,lista,ano);//cambiar 2variable ano por un string que sea igual a lo correspondiente
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte anual del año " + ano +" del cliente" + " " + ventas; //titulo del excel, no del sheet
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/anomes/cliente")
    public ResponseEntity<InputStreamResource> exportDataAnualyMensualCliente(@RequestParam("anomescli")String anomes,@RequestParam("mescli")String mes, @RequestParam("cliente2") String ventas) throws Exception{
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
                mes1="05";
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
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteCliente(dato, ventas);
        String aux="Mes "+mes;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anomes);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte mensual del cliente" + " " + ventas; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+ archivo +".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    @PostMapping("/trimestre/cliente")
    public ResponseEntity<InputStreamResource> exportDataAnualTrimestralCliente(@RequestParam("trimestrecli")String trimestre,@RequestParam("anotricli")String anotri, @RequestParam("cliente3") String ventas) throws Exception{
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
        String mmes1 = anotri +"-"+ mes1;
        String mmes2 = anotri +"-"+ mes2;
        String mmes3 = anotri +"-"+ mes3;
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestreCliente(mmes1,mmes2,mmes3, ventas);
        String aux="Trimestre "+ trimestre ;
        ByteArrayInputStream stream = serviceExcel.exportarData(aux,lista,anotri);
        HttpHeaders headers = new HttpHeaders();
        String archivo = "Reporte trimestral del cliente:" + " " + ventas; //titulo del excel
        headers.add("Content-Disposition","attachment; filename="+archivo+".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }


}
