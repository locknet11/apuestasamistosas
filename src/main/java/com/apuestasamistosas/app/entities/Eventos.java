
package com.apuestasamistosas.app.entities;

import com.apuestasamistosas.app.enums.EstadoEvento;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "eventos")
public class Eventos {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @NotNull
    @OneToOne
    private Equipos equipoA;
    @NotNull
    @OneToOne
    private Equipos equipoB;
    // si es true el evento debe aparecer en el dashboard, por defecto es true
    @Column(columnDefinition = "boolean default true")
    private boolean alta;
    @NotNull
    private LocalDateTime fechaEvento;
    @NotNull
    // si es true entonces el usuario ya no puede apostar en este evento
    private boolean expirado;
    // true = equipoA , false = equipoB, null = empate
    private boolean resultado;
    // el estado puede ser FINALIZADO, ENCURSO o SUSPENDIDO
    @NotNull
    private EstadoEvento estado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Equipos getEquipoA() {
        return equipoA;
    }

    public void setEquipoA(Equipos equipoA) {
        this.equipoA = equipoA;
    }

    public Equipos getEquipoB() {
        return equipoB;
    }

    public void setEquipoB(Equipos equipoB) {
        this.equipoB = equipoB;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public boolean isExpirado() {
        return expirado;
    }

    public void setExpirado(boolean expirado) {
        this.expirado = expirado;
    }

    public boolean isResultado() {
        return resultado;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }
    
    
    
    
}
