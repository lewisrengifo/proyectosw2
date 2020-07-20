package com.example.demo.Controllers;

import com.example.demo.Dto.ArtesanoServiceApi;
import com.example.demo.Entity.*;
import com.example.demo.Repository.ArtesanoRepository;
import com.example.demo.Repository.ComunidadRepository;
import com.example.demo.Repository.ConsignacionyventaRepository;
import com.example.demo.service.ArtesanoService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/artesano")
public class ArtesanoController {

    @Autowired
    ArtesanoService artesanoService;

    @Autowired
    ArtesanoRepository artesanoRepository;
    @Autowired
    ComunidadRepository comunidadRepository;
    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;

    @GetMapping(value = {"", "/lista"})
    public String listaArtesano(Model model, @RequestParam Map<String, Object> params,RedirectAttributes attr) {
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        }catch (NumberFormatException e){
            return "redirect:/artesano/lista";
        }

        int currentPage = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if(currentPage <0){
            return "redirect:/artesano/lista";
        }
        Page<Artesano> page = artesanoService.listAll(currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();


        if (totalPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            if (currentPage > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/artesano";
            }
            model.addAttribute("pages", pages);

        } else {
            attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

            return "redirect:/artesano";
        }
        List<Artesano> listaArtesanos = page.getContent();

        model.addAttribute("totalItems", totalItems);
        model.addAttribute("listaArtesano", listaArtesanos);
        model.addAttribute("current", currentPage + 1);
        model.addAttribute("next", currentPage + 2);
        model.addAttribute("prev", currentPage);
        model.addAttribute("last", totalPages);
        return "artesano/lista";


    }
    
    public boolean isNumeric(String cadena){
        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }


    @GetMapping("/nuevo")
    public String nuevoArtesano(@ModelAttribute("artesano") Artesano a, Model model) {

        model.addAttribute("listacomunidades", comunidadRepository.findAll());
        return "artesano/newEdit";
    }

    @GetMapping("/editar")
    public String editarArtesano(@ModelAttribute("artesano") Artesano artesano,
                                 @RequestParam("id") int id, Model model) {

        Optional<Artesano> optionalArtesano = artesanoRepository.findById(id);
        if (optionalArtesano.isPresent()) {
            artesano = optionalArtesano.get();
            model.addAttribute("artesano", artesano);
            model.addAttribute("listacomunidades", comunidadRepository.findAll());

            return "artesano/newEdit";
        } else {
            return "redirect:/artesano";
        }

    }

    @GetMapping("/borrar")
    public String borrarArtesano(Model model,
                                 @RequestParam("id") String id, RedirectAttributes att) {

        try{
            int idart = Integer.parseInt(id);
            Optional<Artesano> optionalArtesano = artesanoRepository.findById(idart);
            Consignacionyventa consignacionyventa = consignacionyventaRepository.verificaArtesanoEnConsignacionYVenta(idart);
            if (consignacionyventa == null){
                if (optionalArtesano.isPresent()) {
                    att.addFlashAttribute("msgArt", "Artesano borrado Exitosamente");
                    artesanoRepository.deleteById(idart);
                    return "redirect:/artesano";
                }
            }else {
                att.addFlashAttribute("msgArt",
                        "Actualmente el artesano se encuentra en Consignación y Venta");
            }

        }catch (NumberFormatException e){
            return "redirect:/artesano";
        }


        return "redirect:/artesano";
    }

    @PostMapping("/guardar")
    public String guardarArtesano(@ModelAttribute("artesano") @Valid Artesano artesano, BindingResult bindingResult,
                                  RedirectAttributes att, Model model, @RequestParam("comunidad") String idcomunidad) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("listacomunidades", comunidadRepository.findAll());
            return "artesano/newEdit";
        } else {

            if (artesano.getIdartesano() == 0) {
                return agregarNuevoArtesanoYVerificar(artesano, model, att);
            } else {
                //CODIGO ARTESANO MAYUSCULA
                String codm = artesano.getCodigoartesano().toUpperCase();
                artesano.setCodigoartesano(codm);
                Artesano artesanosByCodigo = artesanoRepository.editarArtesanoBuscarCodigo(artesano.getCodigoartesano());
                if (artesano.getIdartesano() == artesanosByCodigo.getIdartesano()) {

                    //NOMBRE ARTESANO 1°MAYUSCULA
                    String nom = artesano.getNombreartesano().substring(0,1).toUpperCase() + artesano.getNombreartesano().substring(1).toLowerCase();
                    artesano.setNombreartesano(nom);
                    //APELLIDO PATERNO 1°MAYUSCULA
                    String ape = artesano.getApellidopaterno().substring(0,1).toUpperCase() + artesano.getApellidopaterno().substring(1).toLowerCase();
                    artesano.setApellidopaterno(ape);
                    //APELLIDO MATERNO 1°MAYUSCULA
                    if (artesano.getApellidomaterno().isEmpty()){
                        artesano.setApellidomaterno(null);
                    }else{
                        String mate = artesano.getApellidomaterno().substring(0,1).toUpperCase() + artesano.getApellidomaterno().substring(1).toLowerCase();
                        artesano.setApellidomaterno(mate);
                    }
                    artesanoRepository.save(artesano);
                } else {
                    if (artesano.getCodigoartesano().equals(artesanosByCodigo.getCodigoartesano())) {
                        model.addAttribute("msgRepetido", "Codigo Artesano ya utilizado");
                        model.addAttribute("listacomunidades", comunidadRepository.findAll());
                        return "artesano/newEdit";
                    } else {
                        att.addFlashAttribute("msgAr", "Artesano Actualizado Exitosamente");
                        //NOMBRE ARTESANO 1°MAYUSCULA
                        String nom = artesano.getNombreartesano().substring(0,1).toUpperCase() + artesano.getNombreartesano().substring(1).toLowerCase();
                        artesano.setNombreartesano(nom);
                        //APELLIDO PATERNO 1°MAYUSCULA
                        String ape = artesano.getApellidopaterno().substring(0,1).toUpperCase() + artesano.getApellidopaterno().substring(1).toLowerCase();
                        artesano.setApellidopaterno(ape);
                        //APELLIDO MATERNO 1°MAYUSCULA
                        if (artesano.getApellidomaterno().isEmpty()){
                            artesano.setApellidomaterno(null);
                        }else{
                            String mate = artesano.getApellidomaterno().substring(0,1).toUpperCase() + artesano.getApellidomaterno().substring(1).toLowerCase();
                            artesano.setApellidomaterno(mate);
                        }

                        artesanoRepository.save(artesano);


                    }
                }
            }
        }
        return "redirect:/artesano";
    }


    public String agregarNuevoArtesanoYVerificar(Artesano artesano, Model model, RedirectAttributes att) {

        //CODIGO ARTESANO MAYUSCULA
        String codm = artesano.getCodigoartesano().toUpperCase();
        artesano.setCodigoartesano(codm);

        List<Artesano> byCodigoartesano = artesanoRepository.buscarSucomunidad(artesano.getCodigoartesano());

        if (byCodigoartesano.isEmpty()) {
            att.addFlashAttribute("msgAr", "Artesano Creado Exitosamente");

            //NOMBRE ARTESANO 1°MAYUSCULA
            String nom = artesano.getNombreartesano().substring(0,1).toUpperCase() + artesano.getNombreartesano().substring(1).toLowerCase();
            artesano.setNombreartesano(nom);
            //APELLIDO PATERNO 1°MAYUSCULA
            String ape = artesano.getApellidopaterno().substring(0,1).toUpperCase() + artesano.getApellidopaterno().substring(1).toLowerCase();
            artesano.setApellidopaterno(ape);
            //APELLIDO MATERNO 1°MAYUSCULA
            if (artesano.getApellidomaterno().isEmpty()){
                artesano.setApellidomaterno(null);
            }else{
                String mate = artesano.getApellidomaterno().substring(0,1).toUpperCase() + artesano.getApellidomaterno().substring(1).toLowerCase();
                artesano.setApellidomaterno(mate);
            }

            artesanoRepository.save(artesano);
            return "redirect:/artesano";
        } else {
            model.addAttribute("msgRepetido", "Codigo ya está siendo utilizado");
            model.addAttribute("listacomunidades", comunidadRepository.findAll());
            return "artesano/newEdit";
        }

    }

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model,RedirectAttributes attr) {
        String busqueda = (String) params.get("searchField");

        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/artesano/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/artesano/lista";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            if(page <0){
                return "redirect:/artesano/lista";
            }
            PageRequest pageRequest = PageRequest.of(page, 10);


            Page<Artesano> pageArt = artesanoRepository.buscadorArtesano(busqueda, pageRequest);
            int totalPage = pageArt.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/artesano/lista";
                }
                model.addAttribute("pages", pages);
            }else{
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/artesano/lista";

            }

            model.addAttribute("listaArtesano", pageArt.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "artesano/lista";
        }
    }



}
