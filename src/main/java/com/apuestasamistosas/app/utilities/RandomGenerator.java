
package com.apuestasamistosas.app.utilities;

import java.util.UUID;


public final class RandomGenerator {
    
    public static String generate(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
