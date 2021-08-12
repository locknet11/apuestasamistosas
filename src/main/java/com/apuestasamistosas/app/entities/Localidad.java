package com.apuestasamistosas.app.entities;

import com.apuestasamistosas.app.entities.direcciones.Direcciones;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "localidades")
public class Localidad {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String localidad;
    
    @ManyToOne
    private Direcciones provincia;

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Direcciones getProvincia() {
        return provincia;
    }

    public void setProvincia(Direcciones provincia) {
        this.provincia = provincia;
    }

}
