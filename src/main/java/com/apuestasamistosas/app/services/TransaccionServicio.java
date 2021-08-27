
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Transaccion;
import com.apuestasamistosas.app.repositories.TransaccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionServicio {
    @Autowired
    private TransaccionRepositorio transaccionRepositorio;
    
    
    @Transactional
    public void crearTransaccion (Double saldo,String idObject){
        
        Transaccion transaccion=new Transaccion();
        transaccion.setSaldo(saldo);
        transaccion.setIdObject(idObject);
        transaccionRepositorio.save(transaccion);
        
    }
    
    
    
    
    
}
