package com.musala.drone.model.exchange;

import com.musala.drone.config.DroneStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
public class DroneExchange {
    private Long id;
    private String serialNumber;
    private String model;
    private Double weightLimit;
    private Integer batteryCapacity;
    private DroneStatus state;
}
