package com.musala.drone.service;

import com.musala.drone.config.DroneStatus;
import com.musala.drone.exception.DroneNotFoundException;
import com.musala.drone.model.Drone;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.model.exchange.PageExchange;
import com.musala.drone.repository.DroneRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {
    public static final String DRONE_IS_NOT_AVAILABLE_FOR_GIVEN_ID_S = "Drone is not available for given id %s";
    private final DroneRepo droneRepo;

    public DroneService(DroneRepo droneRepo) {
        this.droneRepo = droneRepo;
    }

    public DroneExchange registerDrone(DroneExchange droneExchange) {
        Drone drone = buildDrone(droneExchange);
        drone.setState(DroneStatus.IDLE);
        return buildDroneExchange(droneRepo.save(drone));
    }

    public PageExchange<DroneExchange> getAvailableDrones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Drone> dronePage = droneRepo.getAllByStateIn(List.of(DroneStatus.IDLE, DroneStatus.LOADING), pageable);
        return buildDronePageExchange(page, size, dronePage);
    }

    public Integer getBatteryLevel(Long droneId) {
        Optional<Drone> droneOptional = droneRepo.findById(droneId);
        if (droneOptional.isEmpty()) {
            throw new DroneNotFoundException(String.format(DRONE_IS_NOT_AVAILABLE_FOR_GIVEN_ID_S, droneId));
        }
        return droneOptional.get().getBatteryCapacity();
    }

    private PageExchange<DroneExchange> buildDronePageExchange(int page, int size, Page<Drone> dronePage) {
        PageExchange<DroneExchange> dronePageExchange = new PageExchange<>();
        dronePageExchange.setPage(page);
        dronePageExchange.setPageSize(size);
        dronePageExchange.setItems(buildDroneExchanges(dronePage.getContent()));
        dronePageExchange.setTotalItems(dronePage.getTotalElements());
        dronePageExchange.setTotalPages(dronePage.getTotalPages());
        return dronePageExchange;
    }

    private List<DroneExchange> buildDroneExchanges(List<Drone> drones) {
        return drones.stream().map(this::buildDroneExchange).collect(Collectors.toList());
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
        droneExchange.setId(drone.getId());
        droneExchange.setBatteryCapacity(drone.getBatteryCapacity());
        droneExchange.setModel(drone.getModel());
        droneExchange.setState(drone.getState());
        droneExchange.setSerialNumber(drone.getSerialNumber());
        droneExchange.setWeightLimit(drone.getWeightLimit());
        return droneExchange;
    }
}
