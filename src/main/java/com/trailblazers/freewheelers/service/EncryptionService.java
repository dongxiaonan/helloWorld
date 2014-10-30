package com.trailblazers.freewheelers.service;

import org.springframework.security.crypto.codec.Hex;

public class EncryptionService {

    public String getStringToHex(String stringInput) {

        byte[] digestBytes = stringInput.getBytes();
        StringBuffer digestedHex = new StringBuffer();
        for (int i = 0; i < digestBytes.length; i++) {
            if(Double.compare(Math.random(),0.7) < 0 ) {
                digestedHex.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
            } else {
                digestedHex.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1).toUpperCase());
            }
        }
        return digestedHex.toString();
    }

    public String getHexToString(String hexInput) {

        byte[] decodedBytes = Hex.decode(hexInput.toLowerCase());
        try {
        return new String(decodedBytes, "UTF8");
        } catch(Exception e) {
            return "";
        }
    }
}
