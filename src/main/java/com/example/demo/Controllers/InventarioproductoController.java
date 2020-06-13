package com.example.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventarioPrincipal")
public class InventarioproductoController {

    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioProducto(){return "inventario/inventarioPrincipal";}
}
