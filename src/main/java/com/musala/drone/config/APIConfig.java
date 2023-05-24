package com.musala.drone.config;

public interface APIConfig {
    String API = "/api";
    String DRONE = "/drone";
    String API_DRONE = API + DRONE;
    String AVAILABLE = "/available";
    String FINAL_URL_API_DRONE_AVAILABLE = API_DRONE + AVAILABLE;
}
