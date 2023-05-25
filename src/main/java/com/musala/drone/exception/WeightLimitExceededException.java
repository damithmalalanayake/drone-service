package com.musala.drone.exception;

public class WeightLimitExceededException extends RuntimeException {
    public WeightLimitExceededException(String message) {
        super(message);
    }
}
