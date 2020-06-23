package com.example.demo.Controllers;

import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Producto;
import com.example.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/venta")
public class VentasController {

    @Autowired
    ProductoRepository productoRepository;

    @GetMapping(value = {"", "/venta"})
    public String listaArtesano(@ModelAttribute("producto") Producto producto, Model model){
        model.addAttribute("listaArtesano", productoRepository.findAll());
        return "redirect:/venta/venta";
    }

}
