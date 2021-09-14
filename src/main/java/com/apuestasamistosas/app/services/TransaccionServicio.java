
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Transaccion;
import com.apuestasamistosas.app.repositories.TransaccionRepositorio;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionServicio {
   
	@Autowired
    private TransaccionRepositorio transaccionRepositorio;

    @Transactional
    public void crearTransaccion(Double saldo, String idObject) {
        ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDateTime hoy = LocalDateTime.now(argentina);
        Optional<Transaccion> respuesta = transaccionRepositorio.findById(idObject);

        if (respuesta.isPresent()) {

            Transaccion transaccion = new Transaccion();

            Double saldoActualizado = transaccionRepositorio.saldoActual(idObject) + saldo;
            transaccion.setFechaTransaccion(hoy);
            transaccion.setIdObject(idObject);
            transaccion.setSaldo(saldoActualizado);
            transaccionRepositorio.save(transaccion);

        } else {
            Transaccion transaccion = new Transaccion();
            transaccion.setSaldo(saldo);
            transaccion.setIdObject(idObject);
            transaccion.setFechaTransaccion(hoy);
            transaccionRepositorio.save(transaccion);

        }

    }

    public Double saldoActual(Double saldo, String idObject) {

        return transaccionRepositorio.saldoActual(idObject);
    }

    
    
    
    
}
