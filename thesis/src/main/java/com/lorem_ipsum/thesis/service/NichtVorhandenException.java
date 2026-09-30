package com.lorem_ipsum.thesis.service;

public class NichtVorhandenException extends RuntimeException {
    public NichtVorhandenException() {
        super("Ressource nicht gefunden");
    }
    
    public NichtVorhandenException(String message) {
        super(message);
    }
}