package com.example.demo.Controllers;

import com.example.demo.Entity.Producto;
import com.example.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    ProductoRepository productoRepository;

    @GetMapping(value = {"", "/"})
    public String listaProductos(Model model) {
        model.addAttribute("listaProductos", productoRepository.findAll());
        return "producto/listar";
    }
    @GetMapping("/nuevo")
    public String nuevoProductoFrm(Model model) {
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
    public String editarProducto(Model model, @RequestParam("id") int id) {

        Optional<Producto> optProduct = productoRepository.findById(id);

        if (optProduct.isPresent()) {
            Producto producto = optProduct.get();
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
}
