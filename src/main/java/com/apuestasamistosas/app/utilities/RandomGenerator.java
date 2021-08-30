
package com.apuestasamistosas.app.utilities;

import com.apuestasamistosas.app.enums.ResultadoEvento;

import java.util.UUID;


public final class RandomGenerator {
    
    /*  Metodo que genera un UUID aleatorio, se utiliza para generar los codigos de confirmacion */
    
    public static String generateUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
    /*  Metodo para generar un resultado aleatorio entre dos equipos  */
    
    
    /*  Cuando se utiliza en el generador aleatorio los valores de 0, 1, 2 corresponden
        a EQUIPO_A, EQUIPO_B EMPATE respectivamente.
    */
    
    public static ResultadoEvento resultadoAleatorio(){
        Integer aleatorio = (int)(Math.random() * 3) + 1;
        switch(aleatorio){
            case 0:
                return ResultadoEvento.EQUIPO_A;
            case 1:
                return ResultadoEvento.EQUIPO_B;
            case 2:
                return ResultadoEvento.EMPATE;
            default:
                return null;
        }
    }
}
