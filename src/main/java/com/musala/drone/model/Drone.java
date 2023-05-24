package com.musala.drone.model;

import com.musala.drone.config.DroneStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Drone {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    private Long id;
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "weight_limit", nullable = false)
    private Double weightLimit;
    @Column(name = "battery_capacity", nullable = false)
    private Integer batteryCapacity;
    @Column(name = "state", nullable = false)
    private DroneStatus state;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "drone", cascade = CascadeType.ALL)
    private List<Medication> medications;
}
