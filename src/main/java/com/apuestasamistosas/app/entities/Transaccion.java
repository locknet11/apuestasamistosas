
package com.apuestasamistosas.app.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "transacciones")
public class Transaccion {
    
	 	@Id
	    @GeneratedValue(generator = "uuid")
	    @GenericGenerator(name = "uuid", strategy = "uuid2")
	    private String id;
	    private Double saldo;
	    private String idObject;
	    @OneToOne(cascade = CascadeType.ALL)
	    private Apuesta apuesta;
	    private Double precio;
	    
	    public Apuesta getApuesta() {
			return apuesta;
		}

		public void setApuesta(Apuesta apuesta) {
			this.apuesta = apuesta;
		}

		public Double getPrecio() {
			return precio;
		}

		public void setPrecio(Double precio) {
			this.precio = precio;
		}

		@NotNull
	    private LocalDateTime fechaTransaccion;

	    public LocalDateTime getFechaTransaccion() {
	        return fechaTransaccion;
	    }

	    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
	        this.fechaTransaccion = fechaTransaccion;
	    }

	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }

	    public Double getSaldo() {
	        return saldo;
	    }

	    public void setSaldo(Double saldo) {
	        this.saldo = saldo;
	    }

	    public String getIdObject() {
	        return idObject;
	    }

	    public void setIdObject(String idObject) {
	        this.idObject = idObject;
	    }
    
    
    
}
