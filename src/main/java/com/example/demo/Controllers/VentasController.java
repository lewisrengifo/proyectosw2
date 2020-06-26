package com.example.demo.Controllers;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.Dto.ProductoServiceApi;

import com.example.demo.Entity.*;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.ProductoRepository;
import com.example.demo.Repository.ProductoVentaRepository;
import com.example.demo.Repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/venta")
public class VentasController {


    ProductoServiceApi productoServiceApi;

    @Autowired
    VentasRepository ventasRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    ProductoVentaRepository productoVentaRepository;

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;

    @GetMapping(value = {"", "/listaProductosStock"}) //url en la web
    public String listaProductosStock(Model model, RedirectAttributes attr, @ModelAttribute ProductoVenta productoVenta, HttpServletRequest request) {
        List<Inventarioproducto> listaStock = inventarioproductoRepository.findAll();
        model.addAttribute("productoVenta", productoVenta);
        model.addAttribute("listaProductosStock", listaStock);
        return "venta/listaProductosStock"; //Dirección del html en el directorio
    }

    public ArrayList<ProductoVenta> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoVenta> carrito = (ArrayList<ProductoVenta>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    public void guardarCarrito(ArrayList<ProductoVenta> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    @PostMapping(value = "/agregar")
    public String agregarAlCarrito(@ModelAttribute Inventarioproducto inventarioproducto, HttpServletRequest request,
                                   RedirectAttributes redirectAttrs, @ModelAttribute ProductoVenta productoVenta) {

        ArrayList<ProductoVenta> carrito = this.obtenerCarrito(request);
        boolean encontrado = false;
        for (ProductoVenta productosCarrito : carrito) {
            if (productosCarrito.getCodigogenerado() == inventarioproducto.getCodigogenerado()){
                productosCarrito.aumentarCantidad(productoVenta.getCantidad());
                encontrado  = true;
                break;
            }
        }

        if (!encontrado) {
            carrito.add(new ProductoVenta(inventarioproducto.getProducto(), inventarioproducto.getCategoria(), inventarioproducto.getTamano(), inventarioproducto.getColor(), inventarioproducto.getPreciomosqoy(),productoVenta.getCantidad()));
        }
        this.guardarCarrito(carrito, request);
        return "redirect:/venta/";
    }

    @GetMapping(value = {"/verListaCarrito"})
        public String verCarrito(Model model, @ModelAttribute("inventarioproducto") Inventarioproducto inventarioproducto, HttpServletRequest request) {
        model.addAttribute("inventarioproducto", new Inventarioproducto());
        double total = 0;
        ArrayList<ProductoVenta> carritoLista = this.obtenerCarrito(request);
        for (ProductoVenta pd: carritoLista) total += pd.getTotal();
        model.addAttribute("total", total);
        model.addAttribute("carritoLista", carritoLista);
        return "venta/verCarrito";
        }

    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarrito(new ArrayList<>(), request);
    }

    @PostMapping(value = "/terminar")
    public String terminarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoVenta> carrito = this.obtenerCarrito(request);
        if (carrito == null || carrito.size() <= 0) {
            return "redirect:/venta/";
        }
        Ventas ventas = ventasRepository.save(new Ventas());
        ////////////////////////////////POR REVISAR///////////////////////////////
        for (ProductoVenta productoParaVender : carrito) {
            // Obtener el producto fresco desde la base de datos
            //Inventarioproducto pv = inventarioproductoRepository.findById(productoParaVender.getId()).orElse(null);
            //if (pv == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
            // Le restamos existencia
            //p.restarExistencia(productoParaVender.getCantidad());
            // Lo guardamos con la existencia ya restada
            //inventarioproductoRepository.save(pv);
            // Creamos un nuevo producto que será el que se guarda junto con la venta
            //ProductoVenta productoVenta = new ProductoVenta(productoParaVender.getCantidad(), productoParaVender.getPrecio(), productoParaVender.getNombre(), productoParaVender.getCodigo(), v);
            // Y lo guardamos
            //productoVentaRepository.save(productoVenta);
        }
        ////////////////////////////////POR REVISAR///////////////////////////////
        this.limpiarCarrito(request);
        redirectAttrs
                .addFlashAttribute("mensaje", "Venta realizada correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/venta/verCarrito";
    }

    @PostMapping("/search")
    public String buscarProducto(String busca, @RequestParam Map<String, Object> params, Model model, RedirectAttributes attr) {

        String busqueda = (String) params.get("search");

        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/venta";
        }
        PageRequest pageRequest;

        Page<Producto> pageProduct;
        int totalPage;


        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/venta";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/venta";
        }


        pageRequest = PageRequest.of(page, 10);
        pageProduct = productoServiceApi.getEver(busqueda, pageRequest);
        totalPage = pageProduct.getTotalPages();
        if (pageProduct.getTotalElements()==0){

            return "redirect:/venta";
        }
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() -1){
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/venta";
            }
            model.addAttribute("pages", pages);

        }else{
            return "redirect:/venta";

        }

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("listaProductos", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("searchField", busqueda);

        return "venta/listaProductosStock";
    }

    @GetMapping("/search")
    public String buscarProducto(@RequestParam Map<String, Object> params, Model model,RedirectAttributes attr) {

        String busqueda = (String) params.get("search");

        PageRequest pageRequest;

        Page<Producto> pageProduct;
        int totalPage;
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;


        pageRequest = PageRequest.of(page, 10);
        pageProduct = productoServiceApi.getEver(busqueda, pageRequest);
        totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() -1){
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/venta";
            }



            model.addAttribute("pages", pages);
        }else{
            attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

            return "redirect:/venta";
        }

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("listaProductos", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("searchField", busqueda);


        return "venta/listaProductosStock";
    }

}
