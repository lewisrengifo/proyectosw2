package com.example.demo.Controllers;

import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.Producto;
import com.example.demo.Repository.LineaRepository;
import com.example.demo.Repository.ProductoRepository;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    ProductoServiceApi productoServiceApi;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    LineaRepository lineaRepository;

    @GetMapping(value = {"", "/"})
    public String listaProduct(@RequestParam Map<String,Object> params,Model model) {

        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        PageRequest pageRequest = PageRequest.of(page,10);

        Page<Producto> pageProduct = productoServiceApi.getAll(pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if(totalPage > 0 ){
            List<Integer> pages = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages",pages);
        }

        model.addAttribute("listaProductos", pageProduct.getContent());
        model.addAttribute("current",page + 1);
        model.addAttribute("next",page + 2);
        model.addAttribute("prev",page);
        model.addAttribute("last",totalPage);

        return "producto/listar";
    }
    @GetMapping("/nuevo")
    public String nuevoProductoFrm(Model model ,@ModelAttribute("producto") Producto producto) {
       return "producto/editFrm";
    }

    @PostMapping("/guardar")
    public String guardarProducto(Producto producto, RedirectAttributes attr) {
        if (producto.getIdproducto() == 0) {
            attr.addFlashAttribute("msg", "Producto creado exitosamente");
        } else {
            attr.addFlashAttribute("msg", "Producto actualizado exitosamente");
        }

        productoRepository.save(producto);
        return "redirect:/producto";
    }
    @GetMapping("/editar")
    public String editarProducto(Model model, @RequestParam("id") int id, @ModelAttribute("producto") Producto producto) {

        Optional<Producto> optProduct = productoRepository.findById(id);

        if (optProduct.isPresent()) {
             producto = optProduct.get();
            model.addAttribute("producto", producto);
            return "producto/editFrm";
        } else {
            return "redirect:/producto";
        }
    }
    @GetMapping("/borrar")
    public String borrarProducto(Model model,
                                      @RequestParam("id") int id,
                                      RedirectAttributes attr) {

        Optional<Producto> optProduct = productoRepository.findById(id);

        if (optProduct.isPresent()) {
            productoRepository.deleteById(id);
            attr.addFlashAttribute("msg","Producto borrado exitosamente");
        }
        return "redirect:/producto";

    }

    @PostMapping("/search")
    public String buscarProducto (@RequestParam("search") String busqueda,@RequestParam Map<String,Object> params, Model model){


        String search;
        PageRequest pageRequest;
        int  page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        Page<Producto> pageProduct ;
        int totalPage;
        switch (busqueda) {
            case "Tradicional":



                pageRequest = PageRequest.of(page,10);
                pageProduct = productoServiceApi.getEver1(busqueda,pageRequest);
                totalPage = pageProduct.getTotalPages();
                if(totalPage > 0 ){
                    List<Integer> pages = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
                    model.addAttribute("pages",pages);
                }
                model.addAttribute("busqueda", busqueda);
                model.addAttribute("listaProductos", pageProduct.getContent());
                model.addAttribute("current",page + 1);
                model.addAttribute("next",page + 2);
                model.addAttribute("prev",page);
                model.addAttribute("last",totalPage);


                break;
            case "Mosqoy":


                pageRequest = PageRequest.of(page,10);
                pageProduct = productoServiceApi.getEver2(busqueda,pageRequest);
                totalPage = pageProduct.getTotalPages();
                if(totalPage > 0 ){
                    List<Integer> pages = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
                    model.addAttribute("pages",pages);
                }
                model.addAttribute("busqueda", busqueda);
                model.addAttribute("listaProductos", pageProduct.getContent());
                model.addAttribute("current",page + 1);
                model.addAttribute("next",page + 2);
                model.addAttribute("prev",page);
                model.addAttribute("last",totalPage);
                break;
            case "Fibras":


                pageRequest = PageRequest.of(page,10);
                pageProduct = productoServiceApi.getEver3(busqueda,pageRequest);
                totalPage = pageProduct.getTotalPages();
                if(totalPage > 0 ){
                    List<Integer> pages = IntStream.rangeClosed(1,totalPage).boxed().collect(Collectors.toList());
                    model.addAttribute("pages",pages);
                }
                model.addAttribute("busqueda", busqueda);
                model.addAttribute("listaProductos", pageProduct.getContent());
                model.addAttribute("current",page + 1);
                model.addAttribute("next",page + 2);
                model.addAttribute("prev",page);
                model.addAttribute("last",totalPage);
                break;
        }


        return "producto/listar";






    }


}
