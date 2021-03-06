
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
    public static final String NO_CLAVE = "La contraseña no puede estar vacia.";
    public static final String MAIL_REGIST = "Este correo ya se encuentra registrado.";
    public static final String LONG_CLAVE = "La contraseña tiene que tener al menos 8 caracteres";
    public static final String NO_NOMBRE = "El nombre no puede estar vacio";
    public static final String NO_APELLIDO = "El apellido no puede estar vacio";
    public static final String NO_EMAIL = "El correo no puede estar vacío.";
    public static final String NO_DATE = "La fecha de nacimiento de puede ser nula.";
    public static final String COD_CONFIRM = "El codigo de confirmacion es invalido.";
    public static final String NO_FOTO = "Este usuario no tiene foto.";
    public static final String MAX_SIZE = "El tamaño de la foto puede ser de hasta 5MB.";
    public static final String ALREADY_ADMIN = "Este usuario ya es administrador.";
    public static final String USUARIO_BAJA = "Este usuario no esta dado de alta.";
    public static final String USER_MISMATCH = "Este id no corresponde a su cuenta.";
    
    /* IMPLEMENTACION A FUTURO: HABRIA QUE DIVIDIR LOS MENSAJES DE ERROR DE LOS
       MENSAJES INFORMATIVOS, EJ: SE LOGGEA USER X. 
    */
    
}
