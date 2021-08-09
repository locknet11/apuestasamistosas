
package com.apuestasamistosas.app.entities;

import com.apuestasamistosas.app.enums.EstadoApuesta;
import java.time.LocalDateTime;
import javax.persistence.Entity;
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
    private EstadoApuesta estado;
    @OneToOne
    private Usuario usuarioA;
    @OneToOne
    private Usuario usuarioB;
    private LocalDateTime fechaApuesta;
    @OneToOne
    private Premio premio;
    @OneToOne
    private Eventos evento;
    // si es true gana usuarioA si es false gana usuarioB, si es null es porque fue empate
    private boolean usuarioGanador;

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

    public boolean isUsuarioGanador() {
        return usuarioGanador;
    }

    public void setUsuarioGanador(boolean usuarioGanador) {
        this.usuarioGanador = usuarioGanador;
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
    
    
    
    

}
