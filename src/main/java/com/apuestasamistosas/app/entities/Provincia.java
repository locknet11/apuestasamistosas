
package com.apuestasamistosas.app.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "provincias")
public class Provincia {
    
    @Id
    private String provincia;

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    

}
