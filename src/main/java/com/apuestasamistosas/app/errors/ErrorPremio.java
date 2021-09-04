
package com.apuestasamistosas.app.errors;


public class ErrorPremio  extends Exception{

    public ErrorPremio(String message){
        super(message);
    }
    
    public static final String NO_EXISTS = "Este premio no existe.";
    public static final String PRECIO_NEG = "El precio no puede ser cero o negativo.";
    public static final String NO_NOMBRE = "El nombre no puede estar vacio";
    public static final String NULL_FOTO = "La foto del producto no puede estar vacia.";
    
}
