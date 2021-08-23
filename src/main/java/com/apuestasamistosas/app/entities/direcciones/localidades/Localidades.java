package com.apuestasamistosas.app.entities.direcciones.localidades;

import com.apuestasamistosas.app.entities.direcciones.localidades.Centroide;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Localidades {

    @Id
    private String id;
    private String categoria;
    private String fuente;
    private String nombre;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Provincia provincia;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Municipio municipio;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Departamento departamento;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Localidad_censal localidad_censal;
    @OneToOne(cascade = CascadeType.ALL)
    private Centroide centroide;
    

    public Localidades() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Centroide getCentroide() {
        return centroide;
    }

    public void setCentroide(Centroide centroide) {
        this.centroide = centroide;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Localidad_censal getLocalidad_censal() {
        return localidad_censal;
    }

    public void setLocalidad_censal(Localidad_censal localidad_censal) {
        this.localidad_censal = localidad_censal;
    }

}
