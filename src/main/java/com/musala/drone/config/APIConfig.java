package com.musala.drone.config;

public interface APIConfig {
    String API = "/api";
    String DRONE = "/drone";
    String API_DRONE = API + DRONE;
    String AVAILABLE = "/available";
    String BATTERY_LEVEL = "/battery-level";
    String LOAD = "/load";
    String MEDICATIONS = "/medications";
    String FINAL_URL_API_DRONE_AVAILABLE = API_DRONE + AVAILABLE;
    String FINAL_URL_API_DRONE_BATTERY_LEVEL = API_DRONE + BATTERY_LEVEL;
    String FINAL_URL_API_DRONE_LOAD = API_DRONE + LOAD;
    String FINAL_URL_API_DRONE_MEDICATIONS = API_DRONE + MEDICATIONS;
}
