
package com.apuestasamistosas.app.utilities;

import java.util.UUID;


public class RandomGenerator {
    
    public static String generate(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
