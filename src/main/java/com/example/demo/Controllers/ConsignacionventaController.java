package com.example.demo.Controllers;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/ConsignacionVenta")
public class ConsignacionventaController {

    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;

    @Autowired
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @GetMapping(value = {"/lista",""})
    public String listaConsignacionVenta(Model model,@ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        model.addAttribute("listaConsignacionVenta",consignacionyventaRepository.findAll());
        return "consigVenta/consignYventaLista";
    }

    @GetMapping("/agregarProductosCV")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro, @RequestParam("id") int id){

        Optional<Consignacionyventa> consigVentabyId = consignacionyventaRepository.findById(id);

        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());
        model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());

        model.addAttribute("consigYventa1",consigVentabyId.get());
        return "consigVenta/agregarProductos";
    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model,
                                           @ModelAttribute("inventarioProducto") Inventarioproducto invPro,@RequestParam("idconsignacionVenta") int id ){

        Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(id);
        invPro.setConsignacionyventa(ultimaConsigOventa.get());
        Date fechatudei = new Date();

        invPro.setFechainicio(fechatudei);
        if(ultimaConsigOventa.get().getTipo().equals("consignacion")){
            String lineac = invPro.getProducto().getLinea().getCodigolinea();
            String categoriac = invPro.getCategoria().getCodigocategoria();
            String productoc = invPro.getProducto().getCodigoproducto();
            String descriccionC = invPro.getProducto().getCodigodescripcionproducto();
            String tamano = invPro.getTamano().getCodigotamano();
            String comunidadC = invPro.getConsignacionyventa().getArtesano().getComunidad().getCodigocomunidad();
            String artesanoC = invPro.getConsignacionyventa().getArtesano().getCodigoartesano();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            //OBTENER EL MES
            simpleDateFormat = new SimpleDateFormat("MMMM");
            String mesC= simpleDateFormat.format(invPro.getConsignacionyventa().getFechafin()).toUpperCase();
            //OBTENER EL AÑO
            simpleDateFormat = new SimpleDateFormat("YYYY");
            String yearco = simpleDateFormat.format(invPro.getConsignacionyventa().getFechafin()).toUpperCase();
            String totalCodigoGenerado = lineac+categoriac+productoc
                    +descriccionC+tamano+comunidadC+artesanoC+mesC+yearco;
            invPro.setCodigogenerado(totalCodigoGenerado);
        }else{
            String lineac = invPro.getProducto().getLinea().getCodigolinea();
            String categoriac = invPro.getCategoria().getCodigocategoria();
            String productoc = invPro.getProducto().getCodigoproducto();
            String descriccionC = invPro.getProducto().getCodigodescripcionproducto();
            String tamano = invPro.getTamano().getCodigotamano();
            String comunidadC = invPro.getConsignacionyventa().getArtesano().getComunidad().getCodigocomunidad();
            String totalCodigoGenerado = lineac+categoriac+productoc+descriccionC+tamano+comunidadC;
            invPro.setCodigogenerado(totalCodigoGenerado);
        }

        inventarioproductoRepository.save(invPro);
        return "redirect:/ConsignacionVenta";

    }

}
