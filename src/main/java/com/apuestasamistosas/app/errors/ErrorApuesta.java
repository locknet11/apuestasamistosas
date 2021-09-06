package com.apuestasamistosas.app.errors;

public class ErrorApuesta extends Exception {

    public ErrorApuesta(String message) {
        super(message);
    }
    public static final String NULL_evento = "Este evento no existe";
    public static final String NULL_premio= "Este premio no existe";
    public static final String NULL_apuesta= "Esta apuesta no existe";
    public static final String EXPIRADA_APUESTA= "Esta apuesta expirò";
    public static final String CONFIRMADA_APUESTA= "Esta apuesta ya està confirmada";
    public static final String RECHAZADA_APUESTA= "Esta apuesta està rechazada";
}
