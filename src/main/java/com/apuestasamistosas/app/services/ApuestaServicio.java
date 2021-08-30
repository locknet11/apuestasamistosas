
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.repositories.ApuestaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApuestaServicio {
    @Autowired
    private ApuestaRepositorio apuestaRepositorio;
    
    public void  crearApuesta (){
        
    }

}
