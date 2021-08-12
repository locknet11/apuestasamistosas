
package com.apuestasamistosas.app.entities.direcciones;

import com.apuestasamistosas.app.entities.direcciones.Parametros;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;
import lombok.AllArgsConstructor;



@Entity
@AllArgsConstructor
@Table
public class Direcciones {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    private Integer total;
    private Integer cantidad;
    private Integer inicio;
    @OneToMany(cascade = CascadeType.ALL)
    private List <Provincias> provincias;
    @OneToOne(cascade = CascadeType.ALL)
    private Parametros parametros;
       
    public Direcciones() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getInicio() {
        return inicio;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public List <Provincias> getProvincias() {
        return provincias;
    }

    public void setProvincias(List <Provincias> provincias) {
        this.provincias = provincias;
    }

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }
    
    
    
}
