package com.example.demo.Controllers;

import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.*;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioTiendaRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/inventarioSede")
public class InventariosedeController {

    @Autowired
    ProductoServiceApi productoServiceApi;

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;
    @Autowired
    InventarioTiendaRepository inventarioTiendaRepository;


    @GetMapping("/asignarStock")
    public String asignarStock(Model model, @ModelAttribute("sede") Sede sede,
                               @ModelAttribute("inventariosede") Inventariosede inventariosede, HttpSession session) {
        //model.addAttribute("inventario", inventarioSedeRepository.obtenerInvDeMiSedeNormal(usuario.getSede_idsede().getNombre()));
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("inventario", inventarioSedeRepository.obtenerInvDeMiSedeNormal(usuario.getSede_idsede().getNombre()));
        model.addAttribute("listaSede", sedeRepository.listaSedeSinPrincipal(usuario.getSede_idsede().getIdsede()));
        return "sede/asignarStock";
    }

    @PostMapping("/agregarStock")
    public String agregarStock(Model model, @ModelAttribute("inventariosede") Inventariosede inventariosede, @ModelAttribute("sede") Sede sede, RedirectAttributes attr, HttpSession session) {

        Usuario user = (Usuario) session.getAttribute("usuario");
        Inventariosede invs = new Inventariosede();
        invs = inventariosede;
        inventariosede.setEstado("Enviado");
        int cantidadParaSede = inventariosede.getStock();
        int buscaridinventarioPrincipal = inventariosede.getInventarioproductoidinventario().getIdinventario();
        Inventariosede inventarioPrincipalProducto = inventarioSedeRepository.obtenerStockSedePrincipal(user.getSede_idsede().getIdsede(), buscaridinventarioPrincipal);
        int cantidadProductostock = inventarioPrincipalProducto.getStock();


        if (cantidadProductostock >= cantidadParaSede) {
            int stockActualPrincipal = cantidadProductostock - cantidadParaSede;
            // int productoInventario = inventariosede.getInventarioproductoidinventario().getIdinventario();
            int sedePrincipal = user.getSede_idsede().getIdsede();
            Inventariosede inventariosedeCambia = inventarioSedeRepository.ObtenerInventariParacambiarStockParaSede(buscaridinventarioPrincipal, inventariosede.getSede().getIdsede());
            if (inventariosedeCambia == null) {
                inventarioSedeRepository.actualizarStockSede(stockActualPrincipal, inventarioPrincipalProducto.getIdiventariosede());
                inventarioSedeRepository.save(inventariosede);
            } else {

                int nuevoTotal = inventariosedeCambia.getStock() + cantidadParaSede;
                //actuliza el producto de la sede
                inventarioSedeRepository.actualizarStockSede(nuevoTotal, inventariosedeCambia.getIdiventariosede());
                //actualiza el producto en el principal
                inventarioSedeRepository.actualizarStockSede(stockActualPrincipal, inventarioPrincipalProducto.getIdiventariosede());
                attr.addFlashAttribute("msgRepetido", "Se mando producto a sede exitosamente");
            }

        } else {
            attr.addFlashAttribute("msgRepetido", "La Cantidad asignada excede la del producto");
            return "redirect:/inventarioSede/asignarStock";
        }

        return "redirect:/inventarioSede/asignarStock";
    }

    @GetMapping(value = {"", "/lista"})
    public String listaInventarioSede(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // String miSede =  usuario.getSede_idsede().getNombre();

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/inventarioSede";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Inventariosede> pageProduct = inventarioSedeRepository.findAll(pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioSede";
            }
            model.addAttribute("pages", pages);
        } else {

            return "redirect:/inventarioSede";
        }

        model.addAttribute("listaInventarioSede", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return "inventario/inventariosede";


    }

    @GetMapping(value = {"/listarInvMiSede"})
    public String listaInventarioMiSede(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String miSede = usuario.getSede_idsede().getNombre();

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede/listarInvMiSede";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/inventarioSede/listarInvMiSede";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Inventariosede> pageProduct = inventarioSedeRepository.listarInventarioPorSede(usuario.getSede_idsede().getIdsede(), pageRequest);
        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos referidos a su búsqueda");

                return "redirect:/inventarioSede/listarInvMiSede";
            }
            model.addAttribute("pages", pages);
        } else {

            return "redirect:/inventarioSede/listarInvMiSede";
        }

        model.addAttribute("listaInventarioSede", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return "inventario/inventariomisede";

    }

    @GetMapping(value = {"/listarinvsedexconfirmar"})
    public String listaInventariosedexconfirmar(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String miSede = usuario.getSede_idsede().getNombre();

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede/listarinvsedexconfirmar";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/inventarioSede/listarinvsedexconfirmar";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Inventariosede> pageProduct = inventarioSedeRepository.listarInventarioMiSedeProdXconfir(miSede, pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos referidos a su búsqueda");

                return "redirect:/inventarioSede/listarinvsedexconfirmar";
            }
            model.addAttribute("pages", pages);
        } else if (totalPage == 0){
            model.addAttribute("listaInventarioSede", pageProduct.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            return "inventario/inventariosedexconfirmar";
        }else {

            return "redirect:/inventarioSede/listarinvsedexconfirmar";
        }

        model.addAttribute("listaInventarioSede", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalItems", pageProduct.getTotalElements());
        return "inventario/inventariosedexconfirmar";

    }


    @GetMapping("/actualizarEstado")
    public String actualizarEstado(@Param("estado") String estado, @Param("idinventariosede") String idinventariosede, RedirectAttributes att) {
        try {
            int idv = Integer.parseInt(idinventariosede);
            if (estado.equals("recibido")) {
                Optional<Inventariosede> inventSede = inventarioSedeRepository.findById(idv);
                if (inventSede.get().getObservaciones() == null) {
                    inventarioSedeRepository.actualizarEstado(estado, idv);
                    return "redirect:/inventarioSede/listarinvsedexconfirmar";
                } else if (inventSede.get().getObservaciones().isEmpty()) {
                    inventarioSedeRepository.actualizarEstado(estado, idv);
                    return "redirect:/inventarioSede/listarinvsedexconfirmar";
                } else {
                    att.addFlashAttribute("msg", "Para recibir un producto, no debe poseer observaciones");
                    return "redirect:/inventarioSede/listarinvsedexconfirmar";
                }

            } else {
                if (estado.equals("observado")) {
                    Optional<Inventariosede> inventSede = inventarioSedeRepository.findById(idv);

                    if (inventSede.get().getObservaciones() == null) {
                        att.addFlashAttribute("msg", "Debe se escribir las observaciones para mandar el estado Observado");
                        return "redirect:/inventarioSede/listarinvsedexconfirmar";
                    } else if (inventSede.get().getObservaciones().isEmpty()) {
                        att.addFlashAttribute("msg", "Debe se escribir las observaciones para mandar el estado Observado");
                        return "redirect:/inventarioSede/listarinvsedexconfirmar";
                    } else {
                        inventarioSedeRepository.actualizarEstado(estado, idv);
                        return "redirect:/inventarioSede/listarinvsedexconfirmar";
                    }
                } else {
                    return "redirect:/inventarioSede/listarinvsedexconfirmar";
                }

            }
        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede/listarinvsedexconfirmar";
        }

    }

    @GetMapping("/actualizarObservaciones")
    public String actualizarObservaciones(@Param("observaciones") String observaciones, @Param("idinventariosede") String idinventariosede, RedirectAttributes att) {

        try {
            int idve = Integer.parseInt(idinventariosede);

            if (inventarioSedeRepository.findById(idve).isPresent()) {
                inventarioSedeRepository.actualizarObservaciones(observaciones, inventarioSedeRepository.findById(idve).get().getIdiventariosede());
                att.addFlashAttribute("msgObservaciones", observaciones);
                return "redirect:/inventarioSede/listarinvsedexconfirmar";
            } else {
                return "redirect:/inventarioSede/listarinvsedexconfirmar";
            }

        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede/listarinvsedexconfirmar";
        }

    }

    @GetMapping("/buscadorMiSede")
    public String buscadorMisede(HttpSession session, @RequestParam Map<String, Object> params, Model model, RedirectAttributes attr) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String busqueda = (String) params.get("searchField");

        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/inventarioSede/listarInvMiSede";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/inventarioSede/listarInvMiSede";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            if (page < 0) {
                return "redirect:/inventarioSede/listarInvMiSede";
            }

            PageRequest pageRequest = PageRequest.of(page, 10);


            Page<Inventariosede> pageInvSede = inventarioSedeRepository.buscadorInventarioSede(busqueda, usuario.getSede_idsede().getNombre(), pageRequest);
            int totalPage = pageInvSede.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos referidos a su búsqueda");

                    return "redirect:/inventarioSede/listarInvMiSede";
                }
                model.addAttribute("pages", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos referidos a su búsqueda");

                return "redirect:/inventarioSede/listarInvMiSede";


            }

            model.addAttribute("listaInventarioSede", pageInvSede.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "inventario/inventariomisede";
        }
    }


    @GetMapping("/buscadorInvSede")
    public String buscadorsede(HttpSession session, @RequestParam Map<String, Object> params, Model model, RedirectAttributes attr) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String busqueda = (String) params.get("searchField");

        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/inventarioSede";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/inventarioSede";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            if (page < 0) {
                return "redirect:/inventarioSede";
            }

            PageRequest pageRequest = PageRequest.of(page, 10);


            Page<Inventariosede> pageInvSede = inventarioSedeRepository.buscarInvSedes(busqueda, pageRequest);
            int totalPage = pageInvSede.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos referidos a su búsqueda");

                    return "redirect:/inventarioSede";
                }
                model.addAttribute("pages", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos referidos a su búsqueda");

                return "redirect:/inventarioSede";


            }

            model.addAttribute("listaInventarioSede", pageInvSede.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "inventario/inventariosede";
        }
    }


    @GetMapping("devolverPrincipal")

    public String devolverProductoAsedePrincipal(@RequestParam("id") String id,RedirectAttributes att) {

        try{
            int id1 =Integer.parseInt(id);
            Optional<Inventariosede> productEnSede = inventarioSedeRepository.findById(id1);
            Inventariotienda productEnTienda = inventarioTiendaRepository.productoEntiendaTodavia(productEnSede.get().getIdiventariosede());
            if(productEnTienda==null){
                int cantidadSede = productEnSede.get().getStock();
                String principal = "cuzco";
                Inventariosede productoSedePrincipal = inventarioSedeRepository.productoParaDevolverAlPrincipal(productEnSede.get().getInventarioproductoidinventario().getIdinventario(), principal);
                int nuevoTotalEnPrincipal = productoSedePrincipal.getStock() + productEnSede.get().getStock();
                //SE AGREGA LO DE SEDE AL PRINCIPAL EL STOCK
                inventarioSedeRepository.actualizarStockSede(nuevoTotalEnPrincipal, productoSedePrincipal.getIdiventariosede());
                //REDUCIR EL STOCK EN CERO Y PONER ESTADO EN DEVUELTO
                inventarioSedeRepository.cambiaStockyEstadoSedeCuandoDevuelveProduc(0, "devuelto principal", productEnSede.get().getIdiventariosede());
                att.addFlashAttribute("msgPagina","producto devuelto a sede");
                return "redirect:/inventarioSede/listarInvMiSede";
            }else{
                att.addFlashAttribute("msgNosepuedeDevolver","El producto aún se encuentra en la tienda");
                return "redirect:/inventarioSede/listarInvMiSede";
            }



        }catch (NumberFormatException e){
            return "redirect:/inventarioSede/listarInvMiSede";
        }


    }

    @GetMapping("/estadoObservado")
    public String resolverEstadoDeObservado(@RequestParam("resultEstado") String estOb,@RequestParam("idinventariosede") String idinventariosede, RedirectAttributes att){
        try{
            int estaObservado = Integer.parseInt(estOb);
            int idInvSede = Integer.parseInt(idinventariosede);
            //0 es recibir producto y 1, devolver producto a la sede
            if (estaObservado==1){
                String estadoDevolver = "Enviado";
                inventarioSedeRepository.actualizarEstado(estadoDevolver,idInvSede);
                inventarioSedeRepository.actualizarObservaciones(null,idInvSede);
                att.addFlashAttribute("msg","Producto fue devuelto a la sede");
            }
            if (estaObservado == 0) {
                Optional<Inventariosede> inventSedeById = inventarioSedeRepository.findById(idInvSede);
                if (inventSedeById.isPresent()){
                    int stockMandado = inventSedeById.get().getStock();
                    Inventariosede productoDevuelto = inventarioSedeRepository.obtenerStockSedePrincipal(3, inventSedeById.get().getInventarioproductoidinventario().getIdinventario());
                    int totalStockDevolver = stockMandado + productoDevuelto.getStock();
                    inventarioSedeRepository.actualizarStockSede(totalStockDevolver,productoDevuelto.getIdiventariosede());
                    inventarioSedeRepository.deleteById(inventSedeById.get().getIdiventariosede());
                   att.addFlashAttribute("msg","Producto fue devuelto a la sede principal");
                }else{
                    return "redirect:/inventarioSede";
                }
            }
            return "redirect:/inventarioSede";
        }catch (NumberFormatException e){
            return "redirect:/inventarioSede";
        }

    }

}
