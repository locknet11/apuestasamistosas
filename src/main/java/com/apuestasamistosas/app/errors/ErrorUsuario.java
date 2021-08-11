
package com.apuestasamistosas.app.errors;


public class ErrorUsuario extends Exception {

    public ErrorUsuario(String message){
        super(message);
    }
    
    /* Constantes de utilidad para servicios */
    
    public static final String NO_EXISTE = "Este usuario no existe.";
    public static final String NO_ACTIVO = "Este usuario no esta activo.";
    public static final String MAYOR_EDAD = "Tiene que ser mayor de edad para poder registrarse.";
    public static final String DIGIT_TEL = "Solo pueden ser numeros (max. 13)";
    public static final String NO_TEL = "Debe ingresar un numero de teléfono.";
    public static final String DIST_CLAVE = "Las contraseñas no coinciden";
    public static final String MAIL_REGIST = "Este correo ya se encuentra registrado.";
    
    
}
