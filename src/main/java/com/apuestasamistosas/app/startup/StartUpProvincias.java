
package com.apuestasamistosas.app.startup;

import com.apuestasamistosas.app.entities.direcciones.ProvinciasDatos;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

import com.apuestasamistosas.app.repositories.DireccionesRepositorio;
import lombok.extern.java.Log;




//@Component
//@Log
//public class StartUpProvincias implements CommandLineRunner {
//
////    @Value("/json/provincias.json")
////    private String json;
//    @Autowired
//    private DireccionesRepositorio repo;
//    
//    
//
//    @Override
//    public void run(String... args) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        try {
//            /*input para leer el archi json TypeR para deserializar json*/
//            InputStream stream = TypeReference.class.getResourceAsStream("/json/provincias.json");
//            
//           
//            ProvinciasDatos value = mapper.readValue(stream, ProvinciasDatos.class);
//            ProvinciasDatos save = repo.save(value);
//            
//            System.out.println(value.getCantidad());
//
//            log.info(" Provincias info " + save.toString());
//
//        } catch (Exception e) {
//            e.getMessage();
//            System.out.println("salio mal provincias");
//        }
//        
//
//    }
//}
