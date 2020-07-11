package com.example.demo.Controllers;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Repository.ComunidadRepository;
import com.example.demo.Repository.ProductoRepository;
import com.example.demo.Repository.SedeRepository;
import com.example.demo.Repository.VentaRepository1;
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
import java.util.List;

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

    @GetMapping("")
    public String paginaReportes(Model model){
        model.addAttribute("listaComunidad",comunidadRepository.findAll());
        model.addAttribute("listaSede",sedeRepository.findAll());
        model.addAttribute("listaProducto",productoRepository.findAll());
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
        List<ReporteMensualoAnualMosqoyDto> lista= ventaRepository1.reporteTrimestralAnualMosqoy(mes1,mes2,mes3,anotri);
        String aux="Trimestre " +trimestre;
        ByteArrayInputStream stream = serviceExcel.exportarData(anotri,lista,aux);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=Ventas"+aux+anotri+".xls");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
    
}
