
package com.apuestasamistosas.app.errors;


public class ErrorEventos extends Exception{
    
    public ErrorEventos(String message){
        super(message);
    }
    
    public static final String PROV_EXISTS = "Este proveedor ya se encuentra registrado.";
    public static final String DIGIT_TEL = "Solo pueden ser numeros (max. 13)";
    public static final String NO_TEL = "Debe ingresar un numero de teléfono.";
    public static final String NO_NOMBRE = "El nombre no puede estar vacio";
    public static final String NO_RESP = "Debe ingresar un responsable.";
    public static final String NO_PROV = "Este proveedor no existe";
}
