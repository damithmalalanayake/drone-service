package com.musala.drone.exception;

public class DroneNotInValidStateException extends RuntimeException {
    public DroneNotInValidStateException(String message) {
        super(message);
    }
}
