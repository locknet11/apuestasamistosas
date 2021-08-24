
package com.apuestasamistosas.app.entities.direcciones.localidades;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ManyToAny;
import static org.hibernate.engine.internal.Cascade.cascade;

@Entity
@Table
public class LocalidadesDatos {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    private Integer total;
    private Integer cantidad;
    private Integer inicio;
   
    
    @OneToMany(cascade = CascadeType.ALL)
    private List <Localidades> localidades;
    @OneToOne(cascade = CascadeType.ALL)
    private Parametros parametros;
    

    public LocalidadesDatos() {
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

    public List <Localidades> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List <Localidades> localidades) {
        this.localidades = localidades;
    }

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }
    
    
   
}
