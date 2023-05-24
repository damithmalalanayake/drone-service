package com.musala.drone.api;

import com.musala.drone.config.APIConfig;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.model.exchange.PageExchange;
import com.musala.drone.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(APIConfig.API_DRONE)
public class DroneAPI {
    private final DroneService droneService;


    public DroneAPI(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping()
    public ResponseEntity<DroneExchange> registerDrone(@Valid @RequestBody DroneExchange droneExchange) {
        DroneExchange droneExchangeSaved = droneService.registerDrone(droneExchange);
        return ResponseEntity.ok(droneExchangeSaved);
    }

    @GetMapping(APIConfig.AVAILABLE)
    public ResponseEntity<PageExchange<DroneExchange>> getAvailableDronesForLoading(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(droneService.getAvailableDrones(page, pageSize));
    }
}
