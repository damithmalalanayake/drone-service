package com.musala.drone.model.exchange;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MedicationExchange {
    private Long id;
    private String name;
    private Double weight;
    private String code;
    private String imagePath;
}
