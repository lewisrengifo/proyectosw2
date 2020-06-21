package com.example.demo.Controllers;

import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Dto.StorageService;
import com.example.demo.Entity.Producto;
import com.example.demo.Repository.LineaRepository;
import com.example.demo.Repository.ProductoRepository;
import com.example.demo.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
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

    @Autowired
    StorageService storageService;
    @GetMapping(value = {"", "/"})
    public String listaProduct(@RequestParam Map<String, Object> params, Model model) {

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        }catch (NumberFormatException e){
            return "redirect:/producto";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if(page<0){
            return "redirect:/producto";
        }

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Producto> pageProduct = productoServiceApi.getAll(pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("listaProductos", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "producto/listar";
    }

    @GetMapping("/nuevo")
    public String nuevoProductoFrm(Model model, @ModelAttribute("producto") Producto producto) {
        model.addAttribute("listaLinea", lineaRepository.findAll());
        return "producto/editFrm";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@RequestParam("archivo") MultipartFile file, BindingResult bindingResult
            , RedirectAttributes attr, @ModelAttribute ("producto") @Valid Producto producto, Model model) {

       //Ojala salga xd
        HashMap<String, String> map = storageService.store(file);
        if (map.get("estado").equals("exito")) {
            producto.setFoto(map.get("fileName"));
            if (bindingResult.hasErrors()) {
                model.addAttribute("listaLinea", lineaRepository.findAll());
                return "producto/editFrm";
            } else {
                if (producto.getIdproducto() == 0) {
                    for (Producto prod : productoRepository.findAll()) {
                        if (prod.getCodigoproducto().equalsIgnoreCase(producto.getCodigoproducto())) {
                            attr.addFlashAttribute("msg", "Código de producto ya existe");
                            return "redirect:/producto/nuevo";

                        } else if (producto.getIdproducto() == 0) {
                            attr.addFlashAttribute("msgCo", "Producto Creado Exitosamente");
                        } else {
                            attr.addFlashAttribute("msgCo", "Producto Actualizado Exitosamente");
                        }
                    }
                } else {
                    for (Producto prod : productoRepository.mio(producto.getIdproducto())) {
                        if (prod.getCodigoproducto().equalsIgnoreCase(producto.getCodigoproducto())) {
                            if (prod.getNombreproducto().equalsIgnoreCase(producto.getNombreproducto())) {
                                attr.addFlashAttribute("msg1", "Nombre de Comunidad ya exite");
                                attr.addFlashAttribute("comunidad", producto);
                            }
                            return "redirect:/producto/nuevo";

                        } else if (producto.getIdproducto() == 0) {
                            attr.addFlashAttribute("msgCo", "Producto Creado Exitosamente");
                        } else {
                            attr.addFlashAttribute("msgCo", "Producto Actualizado Exitosamente");
                        }
                    }
                }
                String nom = producto.getNombreproducto().substring(0, 1).toUpperCase() + producto.getNombreproducto().substring(1).toLowerCase();
                producto.setNombreproducto(nom);
                String nom1 = producto.getDescripcionproducto().substring(0, 1).toUpperCase() + producto.getDescripcionproducto().substring(1).toLowerCase();
                producto.setDescripcionproducto(nom1);
                String cod = producto.getCodigoproducto().toUpperCase();
                producto.setCodigoproducto(cod);
                String cod1 = producto.getCodigodescripcionproducto().toUpperCase();
                producto.setCodigodescripcionproducto(cod1);
                productoRepository.save(producto);
                return "redirect:/producto";
            }
        }
        else {
            model.addAttribute("msgFoto",map.get("msgFoto"));
            model.addAttribute("listaLinea", lineaRepository.findAll());
            return "producto/editFrm";
        }
}










    @GetMapping("/editar")
    public String editarProducto(Model model, @RequestParam("id") int id, @ModelAttribute("producto") Producto producto) {

        Optional<Producto> optProduct = productoRepository.findById(id);

        if (optProduct.isPresent()) {
            producto = optProduct.get();
            model.addAttribute("producto", producto);
            model.addAttribute("listaLinea", lineaRepository.findAll());
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
            attr.addFlashAttribute("msg", "Producto borrado exitosamente");
        }
        return "redirect:/producto";

    }

    @PostMapping("/search")
    public String buscarProducto(String busca, @RequestParam Map<String, Object> params, Model model) {

        String busqueda = (String) params.get("search");

        PageRequest pageRequest;

        Page<Producto> pageProduct;
        int totalPage;


        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/producto";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/producto";
        }


        pageRequest = PageRequest.of(page, 10);
        pageProduct = productoServiceApi.getEver(busqueda, pageRequest);
        totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("listaProductos", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "producto/listar";
    }

    @GetMapping("/search")
    public String buscarProducto(@RequestParam Map<String, Object> params, Model model) {

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
            model.addAttribute("pages", pages);
        }

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("listaProductos", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "producto/listar";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes att) {
        String returnValue = "redirect:/producto";
        Path pathFinal = null;
        // File  f = null;
        Producto producto = new Producto();
        producto.setFoto(imageFile.getOriginalFilename());


        try {
            pathFinal = productoServiceApi.saveImage(imageFile, producto);
            byte[] bytes = imageFile.getBytes();
            Files.write(pathFinal, bytes);
            // f = new File(pathFinal.toString());
            // BufferedImage image = ImageIO.read(f);
            // int height = image.getHeight();
            // int width = image.getWidth();


        } catch (Exception e) {

            att.addFlashAttribute("msgImagenProducto", "La imagen seleccionada no existe o no es válida");
            return "producto/editFrm";
        }


        return returnValue;
    }
    @GetMapping("/foto")
    public String mostrarFoto(@RequestParam("id")int id, Model model,@ModelAttribute("producto") Producto producto) {

        Optional<Producto> optProduct = productoRepository.findById(id);
        int total=productoRepository.findAll().size();
        int pagina=0;
        int ii=1;
        int a=0;
        while(ii<=(total/10)+1){
            if( (ii-a+10*(ii-1)<=id) && (id<=ii*10) ){
                pagina=ii;
                break;
            }
            else{
                a++;
                ii=ii+1;
            }
        }
        if (optProduct.isPresent()) {
            producto =optProduct.get();
            model.addAttribute("producto",producto);
            model.addAttribute("pagina",pagina);
        }
        return "producto/foto";
    }

    /*@GetMapping("/fotoProducto")
    public String darArchivo(@RequestParam("id")int id){

        return"producto/fotoProducto";
    }*/
    @GetMapping(value = "/fotoProducto", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> getFile(@RequestParam("id")int id) throws IOException {
        Optional<Producto> optProduct = productoRepository.findById(id);
        Producto producto=optProduct.get();
        File file = new File("C:\\FotosProyecto\\"+ producto.getFoto());
        HttpHeaders respHeaders = new HttpHeaders();
        return new ResponseEntity<FileSystemResource>(
                new FileSystemResource(file), respHeaders, HttpStatus.OK
        );
    }


}





