package com.musala.drone.model.exchange;

import com.musala.drone.config.DroneStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;

@Getter
@Setter
public class DroneExchange {
    public static final String ERROR_MESSAGE_DRONE_MUST_HAVE_ONLY_500_G_MAX_WEIGHT_LIMIT = "Drone must have only 500g max weight limit";
    public static final String ERROR_MESSAGE_BATTERY_CAPACITY_SHOULD_BE_WITHIN_100_AS_IT_SHOWS_A_PERCENTAGE = "Battery capacity should be within 100, as it shows a percentage";
    private Long id;
    private String serialNumber;
    private String model;
    @Max(value = 500, message = ERROR_MESSAGE_DRONE_MUST_HAVE_ONLY_500_G_MAX_WEIGHT_LIMIT)
    private Double weightLimit;
    @Max(value = 100, message = ERROR_MESSAGE_BATTERY_CAPACITY_SHOULD_BE_WITHIN_100_AS_IT_SHOWS_A_PERCENTAGE)
    private Integer batteryCapacity;
    private DroneStatus state;
}
