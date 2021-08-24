package com.apuestasamistosas.app.entities.direcciones;

//import com.apuestasamistosas.app.entities.direcciones.Centroide;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Provincias {

    @Id  
    private String id;
    private String nombre_completo;
    private String fuente;
    private String iso_id;
    private String nombre;    
    private String categoria;
    private String iso_nombre;
//    @OneToOne(cascade = CascadeType.ALL)
//    private Centroide centroide;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getIso_id() {
        return iso_id;
    }

    public void setIso_id(String iso_id) {
        this.iso_id = iso_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIso_nombre() {
        return iso_nombre;
    }

    public void setIso_nombre(String iso_nombre) {
        this.iso_nombre = iso_nombre;
    }

//    public Centroide getCentroide() {
//        return centroide;
//    }
//
//    public void setCentroide(Centroide centroide) {
//        this.centroide = centroide;
//    }
//    
    
}
