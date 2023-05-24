package com.musala.drone.service;

import com.musala.drone.config.DroneStatus;
import com.musala.drone.model.Drone;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.repository.DroneRepo;
import org.springframework.stereotype.Service;

@Service
public class DroneService {
    private final DroneRepo droneRepo;

    public DroneService(DroneRepo droneRepo) {
        this.droneRepo = droneRepo;
    }

    public DroneExchange registerDrone(DroneExchange droneExchange) {
        Drone drone = buildDrone(droneExchange);
        drone.setState(DroneStatus.IDLE);
        return buildDroneExchange(droneRepo.save(drone));
    }

    private Drone buildDrone(DroneExchange droneExchange) {
        Drone drone = new Drone();
        drone.setBatteryCapacity(droneExchange.getBatteryCapacity());
        drone.setModel(droneExchange.getModel());
        drone.setSerialNumber(droneExchange.getSerialNumber());
        drone.setWeightLimit(droneExchange.getWeightLimit());
        return drone;
    }

    private DroneExchange buildDroneExchange(Drone drone) {
        DroneExchange droneExchange = new DroneExchange();
        droneExchange.setBatteryCapacity(drone.getBatteryCapacity());
        droneExchange.setModel(drone.getModel());
        droneExchange.setState(drone.getState());
        droneExchange.setSerialNumber(drone.getSerialNumber());
        droneExchange.setWeightLimit(drone.getWeightLimit());
        return droneExchange;
    }
}
