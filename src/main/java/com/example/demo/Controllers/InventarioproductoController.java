package com.example.demo.Controllers;

import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.service.InventarioPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/inventarioPrincipal")
public class InventarioproductoController {

    @Autowired
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    ArtesanoRepository artesanoRepository;
    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    InventarioPrincipalService inventarioPrincipalService;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;



    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioProducto(Model model,@ModelAttribute("consigYVenta") Consignacionyventa consigYventa, @RequestParam Map<String, Object> params){

        int currentPage = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        Page<Inventarioproducto> page = inventarioPrincipalService.listAll(currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        List<Inventarioproducto> listaInventarioPrincipal = page.getContent();


        model.addAttribute("totalItems", totalItems);
        model.addAttribute("listaInventarioPrincipal", listaInventarioPrincipal);
        model.addAttribute("current", currentPage + 1);
        model.addAttribute("next", currentPage + 2);
        model.addAttribute("prev", currentPage);
        model.addAttribute("last", totalPages);
        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/agregarInventario")
    public String consignacionYVenta( @ModelAttribute("referencia2") String referencia2, @ModelAttribute("consigYVenta") Consignacionyventa consigYventa, Model model , @RequestParam("referencia") int referencia ){

        if(referencia == 1){
            model.addAttribute("listaArtesano",artesanoRepository.findAll());
            referencia2 = "consig";

        return "inventario/consig";}
        else {
            model.addAttribute("listaArtesano",artesanoRepository.findAll());
            return "inventario/comprado";

        }
    }

    @PostMapping("/agregarConsigVenta")
    public String ingresarConsignacionOventa(@RequestParam("referencia2") String referencia2 ,Model model,@ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                               @ModelAttribute("consigYVenta") Consignacionyventa consigYventa) throws ParseException {

        if (referencia2.equals("consig")) {
            consigYventa.setTipo("Consignación");
            Consignacionyventa save = consignacionyventaRepository.save(consigYventa);
            int idultimo = save.getIdconsignacion();

            return "redirect:/inventarioPrincipal/sgteProductos/" + idultimo;
        }else{
            consigYventa.setTipo("Comprado");
            String fecha= "22/12/1900";
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Date fechafin = null;
                try {
                    fechafin = formato.parse(fecha);
                }
                catch (ParseException ex)
                {
                    System.out.println(ex);

                    return "redirect:/inventarioPrincipal";
                }
            Consignacionyventa save = consignacionyventaRepository.save(consigYventa);
            int idultimo = save.getIdconsignacion();
            return "redirect:/inventarioPrincipal/sgteProductos/" + idultimo;
            }




        }


    @GetMapping("/sgteProductos/{idultimo}")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                        @ModelAttribute("consigYVenta") Consignacionyventa consigYventa,@PathVariable("idultimo") int id){
        Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(id);

        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());
      model.addAttribute("consigYventa1",ultimaConsigOventa.get());
        return "inventario/inventarioProducto";
    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                           @ModelAttribute("consigYVenta") Consignacionyventa consigYventa,@RequestParam("idconsignacionVenta") int id,HttpSession session){

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
        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        Inventarioproducto invProductoUltimo = inventarioproductoRepository.save(invPro);

        Inventariosede inventariosede= new Inventariosede();
        inventariosede.setStock(invProductoUltimo.getCantidad());
        inventariosede.setFechallegada(fechatudei);
        inventariosede.setInventarioproductoidinventario(invProductoUltimo);
        inventariosede.setEstado("entregado");
        inventariosede.setSede(usuariologueado.getSede_idsede());
        inventarioSedeRepository.save(inventariosede);

        return "redirect:/inventarioPrincipal/sgteProductos/"+ultimaConsigOventa.get().getIdconsignacion();

    }

    @PostMapping("/confirmarPedido")
    public String confirmacionPedidos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                      @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        return "inventario/confirmarpedido";
    }


    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model) {

        String busqueda = (String) params.get("searchField");
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        Page<Inventarioproducto> pageInvPrincipal = inventarioPrincipalService.listSearch(busqueda, page);
        int totalPage = pageInvPrincipal.getTotalPages();
        long totalItems = pageInvPrincipal.getTotalElements();

        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }



        model.addAttribute("totalItems", totalItems);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("listaInventarioPrincipal", pageInvPrincipal.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/stock")
    public String stockDeProductosDisponiblesParaSedes(Model model,HttpSession session){
        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("stockProductos",inventarioSedeRepository.
                listarInventarioPorSede(usuariologueado.getSede_idsede().getIdsede()));
        return "inventario/stockProductoInvPrincipal";
    }






}
