
package com.apuestasamistosas.app.entities;

import com.apuestasamistosas.app.enums.EstadoEvento;
import com.apuestasamistosas.app.enums.ResultadoEvento;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private String nombre;
   
    @NotNull
    @OneToOne
    private Equipos equipoA;
   
    @NotNull
    @OneToOne
    private Equipos equipoB;
    
    /*
       si es true el evento debe aparecer en el dashboard, por defecto es true
    */
    
    @Column(columnDefinition = "boolean default true")
    private Boolean alta;
    
    @NotNull
    private LocalDateTime fechaEvento;
    
    /* 
        si es true entonces el usuario ya no puede apostar en este evento
    */
    
    @NotNull
    @Column(columnDefinition = "boolean default false")
    private boolean expirado;
    
    /*  El resultado puede ser EQUIPO_A, EQUIPO_B, EMPATE o INDEFINIDO */
    
    @Enumerated(EnumType.STRING)
    private ResultadoEvento resultado;

    /* 
        el estado puede ser FINALIZADO, ENCURSO o SUSPENDIDO
    */ 
    
    @Enumerated(EnumType.STRING)
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

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Eventos{" + "id=" + id + ", nombre=" + nombre + ", equipoA=" + equipoA + ", equipoB=" + equipoB + ", alta=" + alta + ", fechaEvento=" + fechaEvento + ", expirado=" + expirado + ", resultado=" + resultado + ", estado=" + estado + '}';
    }

    public ResultadoEvento getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoEvento resultado) {
        this.resultado = resultado;
    }
    
    
    
    
}
