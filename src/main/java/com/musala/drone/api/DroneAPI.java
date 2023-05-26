package com.musala.drone.api;

import com.musala.drone.config.APIConfig;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.model.exchange.MedicationExchange;
import com.musala.drone.model.exchange.PageExchange;
import com.musala.drone.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(APIConfig.API_DRONE)
@Slf4j
public class DroneAPI {
    private final DroneService droneService;


    public DroneAPI(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping()
    public ResponseEntity<DroneExchange> registerDrone(@Valid @RequestBody DroneExchange droneExchange) {
        log.info("REST request to register drone - {}", droneExchange);
        DroneExchange droneExchangeSaved = droneService.registerDrone(droneExchange);
        return ResponseEntity.ok(droneExchangeSaved);
    }

    @GetMapping(APIConfig.AVAILABLE)
    public ResponseEntity<PageExchange<DroneExchange>> getAvailableDronesForLoading(@RequestParam Integer page, @RequestParam Integer pageSize) {
        log.info("REST request to get available drones for loading - page {}, pageSize {}", page, pageSize);
        return ResponseEntity.ok(droneService.getAvailableDrones(page, pageSize));
    }

    @GetMapping(APIConfig.BATTERY_LEVEL + "/{droneId}")
    public ResponseEntity<Integer> getBatteryLevel(@PathVariable("droneId") Long droneId) {
        log.info("REST request to get battery level - {}", droneId);
        return ResponseEntity.ok(droneService.getBatteryLevel(droneId));
    }

    @PatchMapping(APIConfig.LOAD + "/{droneId}")
    public ResponseEntity<DroneExchange> loadDroneWithMedications(@PathVariable("droneId") Long droneId, @RequestBody List<MedicationExchange> medicationExchanges) {
        log.info("REST request to load drones with medications - drone id {}, medications - {}", droneId, medicationExchanges);
        return ResponseEntity.ok(droneService.loadDroneWithMedicationItems(droneId, medicationExchanges));
    }

    @GetMapping(APIConfig.MEDICATIONS + "/{droneId}")
    public ResponseEntity<List<MedicationExchange>> getALlMedicationsForGivenDrone(@PathVariable("droneId") Long droneId) {
        log.info("REST request to get all medications for given drone - {}", droneId);
        return ResponseEntity.ok(droneService.getAllMedicationsLoadedByDroneId(droneId));
    }
}
