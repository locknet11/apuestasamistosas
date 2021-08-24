
package com.apuestasamistosas.app.entities.direcciones.localidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;




@Entity
@Table
public class Ciudades {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    private Integer total;
    private Integer cantidad;
    private Integer inicio;
    @OneToMany(cascade = CascadeType.ALL)
    private List <LocalidadesDatos> localidades;
    @OneToOne(cascade = CascadeType.ALL)
    private Parametros parametros;
       
    public Ciudades() {
    }

   
    
    
}
