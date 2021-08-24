package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Foto;
import com.apuestasamistosas.app.repositories.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    public Foto guardar(MultipartFile archivo) throws Exception {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                
            }

        }
        return null;

    }
    
    public Foto actualizar (String idfoto,MultipartFile archivo) throws Exception {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                if (idfoto !=null){
                    Optional <Foto> respuesta= fotoRepositorio.findById(idfoto);
                    if (respuesta.isPresent()) {
                        respuesta.get();
                        
                    }
            }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                
            }

        }
        return null;
    }

}