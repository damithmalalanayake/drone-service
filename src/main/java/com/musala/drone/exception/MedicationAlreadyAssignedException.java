package com.musala.drone.exception;

public class MedicationAlreadyAssignedException extends RuntimeException {
    public MedicationAlreadyAssignedException(String message) {
        super(message);
    }
}
