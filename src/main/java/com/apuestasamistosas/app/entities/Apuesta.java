
package com.apuestasamistosas.app.entities;

import com.apuestasamistosas.app.enums.EstadoApuesta;
import com.apuestasamistosas.app.enums.ResultadoApuesta;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "apuestas")
public class Apuesta {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Enumerated(EnumType.STRING)
    private EstadoApuesta estado;
    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuarioA;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuarioB;
    
    private LocalDateTime fechaApuesta;
    
    @OneToOne
    private Premio premio;
   
    @OneToOne(cascade = CascadeType.ALL)
    private Eventos evento;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Equipos equipoElegidoPorUsuarioA;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Equipos equipoElegidoPorUsuarioB;
    
    @Enumerated(EnumType.STRING)
    private ResultadoApuesta resultadoApuesta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EstadoApuesta getEstado() {
        return estado;
    }

    public void setEstado(EstadoApuesta estado) {
        this.estado = estado;
    }

   
    public LocalDateTime getFechaApuesta() {
        return fechaApuesta;
    }

    public void setFechaApuesta(LocalDateTime fechaApuesta) {
        this.fechaApuesta = fechaApuesta;
    }

    public Premio getPremio() {
        return premio;
    }

    public void setPremio(Premio premio) {
        this.premio = premio;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }


    public ResultadoApuesta getResultadoApuesta() {
		return resultadoApuesta;
	}

	public void setResultadoApuesta(ResultadoApuesta resultadoApuesta) {
		this.resultadoApuesta = resultadoApuesta;
	}

	public Usuario getUsuarioA() {
        return usuarioA;
    }

    public void setUsuarioA(Usuario usuarioA) {
        this.usuarioA = usuarioA;
    }

    public Usuario getUsuarioB() {
        return usuarioB;
    }

    public void setUsuarioB(Usuario usuarioB) {
        this.usuarioB = usuarioB;
    }

	public Equipos getEquipoElegidoPorUsuarioA() {
		return equipoElegidoPorUsuarioA;
	}

	public void setEquipoElegidoPorUsuarioA(Equipos equipoElegidoPorUsuarioA) {
		this.equipoElegidoPorUsuarioA = equipoElegidoPorUsuarioA;
	}

	public Equipos getEquipoElegidoPorUsuarioB() {
		return equipoElegidoPorUsuarioB;
	}

	public void setEquipoElegidoPorUsuarioB(Equipos equipoElegidoPorUsuarioB) {
		this.equipoElegidoPorUsuarioB = equipoElegidoPorUsuarioB;
	}
    
    
    
    

}
