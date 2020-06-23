package com.example.demo.Controllers;

import com.example.demo.Repository.ConsignacionyventaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ConsignacionVenta")
public class ConsignacionventaController {

    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;

    @GetMapping(value = {"/lista",""})
    public String listaConsignacionVenta(Model model){
        model.addAttribute("listaConsignacionVenta",consignacionyventaRepository.findAll());
        return "";
    }

}
