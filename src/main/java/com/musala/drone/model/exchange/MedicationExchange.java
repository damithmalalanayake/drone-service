package com.musala.drone.model.exchange;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationExchange {
    private Long id;
    private String name;
    private Double weight;
    private String code;
    private String imagePath;
}
