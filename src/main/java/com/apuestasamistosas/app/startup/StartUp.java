
package com.apuestasamistosas.app.startup;

import com.apuestasamistosas.app.entities.direcciones.Direcciones;
import com.apuestasamistosas.app.repositories.ProvinciaRepositorio;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import lombok.extern.java.Log;



@Component
@Log
public class StartUp implements CommandLineRunner {

//    @Value("/json/provincias.json")
//    private String json;
    @Autowired
    private ProvinciaRepositorio repo;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {

            InputStream stream = TypeReference.class.getResourceAsStream("/json/provincias.json");

            Direcciones value = mapper.readValue(stream, Direcciones.class);
            Direcciones save = repo.save(value);
            System.out.println(value.getCantidad());

            log.info(" Student info " + save.toString());

        } catch (Exception e) {
            e.getMessage();
            System.out.println("salio mal papu");
        }

    }
}
