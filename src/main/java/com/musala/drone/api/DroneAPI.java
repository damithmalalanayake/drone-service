package com.musala.drone.api;

import com.musala.drone.config.APIConfig;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(APIConfig.API_DRONE)
public class DroneAPI {
    private final DroneService droneService;


    public DroneAPI(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping()
    public ResponseEntity<?> registerDrone(@Valid @RequestBody DroneExchange droneExchange) {
        DroneExchange droneExchangeSaved = droneService.registerDrone(droneExchange);
        return ResponseEntity.ok(droneExchangeSaved);
    }
}
