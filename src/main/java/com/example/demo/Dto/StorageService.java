package com.example.demo.Dto;


import org.apache.poi.util.IOUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.HashMap;

@Component
public class StorageService {
    //Se detalla donde estaran guardadas las imagenes
    String fileLocation = "/home/ec2-user/FotosProyecto/";
    //String fileLocation = "C:/FotosProyecto/";



    //Este metodo hace la logica de guardar la imagen y lo devuelve como un hashmap
    public HashMap<String,String> store (MultipartFile file){
        HashMap<String,String> map = new HashMap<>();

        //Aqui obtenemos el nombre del archivo con su extension
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //fileName=encriptar(fileName)+".jpg";
        int numeroAleatorio = (int) (Math.random()*999999999+1);
        String aleatorio= String.valueOf(numeroAleatorio);
        String fileName=aleatorio+".jpg";
        /*SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String newname = bytes.toString();*/

        //Validamos si el archivo esta vacio
        try{
            if(file.isEmpty()){
                map.put("estado","error");
                map.put("msgFoto","No se puede guardar un archivo vacio: "+ fileName);
            }
            else if(fileName.contains("..")){
                map.put("estado","error");
                map.put("msgFoto","No se permiten '..' en el archivo");
            }else{

                try(InputStream inputStream = file.getInputStream()){
                    //byte[] byteArray = IOUtils.toByteArray(inputStream);

                    Path filePath = Paths.get(fileLocation);
                    Files.copy(inputStream,filePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    map.put("estado","exito");
                    map.put("fileName",fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    public String encriptar(String pww) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        pww = bCryptPasswordEncoder.encode(pww);
        return pww;
    }
}
