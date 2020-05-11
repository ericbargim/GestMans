package com.gestmans.Business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityHelper {
    private static String toHexadecimal(byte[] digest){
        String hash = "";
        for(byte aux : digest) { //Recorre el array, leyendo de 2 en 2  bytes (Hexadecimal)
            int b = aux & 0xff; // Transforma el valor en entero
            if (Integer.toHexString(b).length() == 1) hash += "0";  // por si solo hay un byte
            hash += Integer.toHexString(b); // convierte el entero a Hexadecimal, mostrandolo como String

        }
        return hash;
    }
    
    public String getStringMessageDigest(String message, String algorithm){
        byte[] digest = null;
        byte[] buffer = message.getBytes();  // Convierte el mensaje a direccion de matriz de bytes pues asi trabaja MessageDigest
        try {
            // Se le pasa el tipo de algoritmo hash a aplicar
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // Reset del digest o resumen que pueda existir en el objeto
            messageDigest.reset();
            // Se envia el mensaje a aplicar alalgoritmo hash
            messageDigest.update(buffer);
            // Se obtiene el resumen del mensaje en matriz de bytes
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest); //Convierte el resumen del mensaje en Bytes (Integer) a String (Hexadecimal)
    }
}
