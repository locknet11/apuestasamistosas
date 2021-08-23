
package com.apuestasamistosas.app.startup;


import com.apuestasamistosas.app.entities.direcciones.localidades.LocalidadesDatos;
import com.apuestasamistosas.app.repositories.LocalidadesRepositorio;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;




@Component
@Log
public class StartUpLocalidades implements CommandLineRunner {

    @Value("${demo.json.string}")
    private String json;
    
    @Autowired
    private LocalidadesRepositorio repo;
    

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            /*input para leer el archi json TypeR para deserializar json*/
            InputStream stream = TypeReference.class.getResourceAsStream("/json/localidades.json");
            
           
            LocalidadesDatos value = mapper.readValue(stream, LocalidadesDatos.class);
            
            LocalidadesDatos save = repo.saveAndFlush(value);
            
            

            log.info(" Provincias info " + save.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("salio mal localidades");
            
        }
        

    }
}
