package com.apuestasamistosas.app.errors;

public class ErrorEquipos extends Exception {

    public ErrorEquipos(String message) {
        super(message);
    }

    public static final String EQP_EXISTS = "Este equipo ya se encuentra registrado";
    public static final String NULL_SPORT = "El deporte no puede estar vacío o ser nulo";
    public static final String NULL_EQP = "El nombre del equipo no puede estar vacío";
    public static final String NULL_FOTO = "Debe seleccionar un escudo o bandera";
    public static final String NO_EXISTS = "Este equipo no existe.";

}
