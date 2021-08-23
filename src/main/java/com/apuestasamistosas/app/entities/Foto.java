package com.apuestasamistosas.app.entities;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Foto {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String mime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

//    @Temporal(TemporalType.DATE)
//    private Date creado;
//
//    @Temporal(TemporalType.DATE)
//    private Date editado;
//
//    private boolean activo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public byte[] getContent() {
        return getContenido();
    }

    public void setContent(byte[] contenido) {
        this.setContent(contenido);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.setContenido(contenido);
    }

    
    
    

}
