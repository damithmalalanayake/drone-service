package com.musala.drone.service;

import com.musala.drone.config.DroneStatus;
import com.musala.drone.exception.*;
import com.musala.drone.model.Drone;
import com.musala.drone.model.Medication;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.model.exchange.MedicationExchange;
import com.musala.drone.model.exchange.PageExchange;
import com.musala.drone.repository.DroneRepo;
import com.musala.drone.repository.MedicationRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {
    public static final String DRONE_IS_NOT_AVAILABLE_FOR_GIVEN_ID_S = "Drone is not available for given id %s";
    public static final String ERROR_MESSAGE_DRONE_SHOULD_BE_IN_IDLE_STATE_BUT_IT_S_IN_S = "Drone should be in IDLE state, But it's in %s";
    public static final String ERROR_MESSAGE_DRONE_BATTERY_LEVEL_SHOULD_BE_MORE_THAN_25_BUT_IT_HAS_ONLY_S = "Drone battery level should be more than 25, But it has only %s";
    public static final String ERROR_MESSAGE_MEDICATION_IS_NOT_AVAILABLE_FOR_GIVEN_IDS = "Medication is not available for given ids";
    public static final String ERROR_MESSAGE_WEIGHT_LIMIT_EXCEEDED_WITH_PROVIDED_MEDICATIONS = "Weight limit exceeded with provided medications!";
    public static final String ERROR_MESSAGE_SOME_OF_THE_GIVEN_MEDICATIONS_ARE_ALREADY_ASSIGNED_TO_A_DRONE = "Some of the given medications are already assigned to a drone!";
    private final DroneRepo droneRepo;
    private final MedicationRepo medicationRepo;

    public DroneService(DroneRepo droneRepo, MedicationRepo medicationRepo) {
        this.droneRepo = droneRepo;
        this.medicationRepo = medicationRepo;
    }

    public DroneExchange registerDrone(DroneExchange droneExchange) {
        Drone drone = buildDrone(droneExchange);
        drone.setState(DroneStatus.IDLE);
        return buildDroneExchange(droneRepo.save(drone));
    }

    public DroneExchange loadDroneWithMedicationItems(Long droneId, List<MedicationExchange> medicationExchange) {
        Optional<Drone> droneOptional = droneRepo.findById(droneId);
        if (droneOptional.isEmpty()) {
            throw new DroneNotFoundException(String.format(DRONE_IS_NOT_AVAILABLE_FOR_GIVEN_ID_S, droneId));
        }
        Drone drone = droneOptional.get();
        if (!drone.getState().equals(DroneStatus.IDLE)) {
            throw new DroneNotInValidStateException(String.format(ERROR_MESSAGE_DRONE_SHOULD_BE_IN_IDLE_STATE_BUT_IT_S_IN_S, drone.getState()));
        }
        if (drone.getBatteryCapacity() < 25) {
            throw new DroneBatteryLevelException(String.format(ERROR_MESSAGE_DRONE_BATTERY_LEVEL_SHOULD_BE_MORE_THAN_25_BUT_IT_HAS_ONLY_S, drone.getBatteryCapacity()));
        }
        List<Long> medicationIds = medicationExchange.parallelStream().map(MedicationExchange::getId).collect(Collectors.toList());
        List<Medication> medicationList = medicationRepo.findAllById(medicationIds);

        if (medicationIds.size() != medicationList.size()) {
            throw new MedicationNotFoundException(ERROR_MESSAGE_MEDICATION_IS_NOT_AVAILABLE_FOR_GIVEN_IDS);
        }

        if (medicationList.parallelStream().anyMatch(medication -> !Objects.isNull(medication.getDrone()))) {
            throw new MedicationAlreadyAssignedException(ERROR_MESSAGE_SOME_OF_THE_GIVEN_MEDICATIONS_ARE_ALREADY_ASSIGNED_TO_A_DRONE);
        }

        if (isWeightLimitExceeded(drone, medicationList)) {
            throw new WeightLimitExceededException(ERROR_MESSAGE_WEIGHT_LIMIT_EXCEEDED_WITH_PROVIDED_MEDICATIONS);
        }
        medicationList.parallelStream().forEach(medication -> medication.setDrone(drone));
        drone.setMedications(medicationList);
        drone.setState(DroneStatus.LOADED);
        return buildDroneExchange(droneRepo.save(drone));
    }

    public PageExchange<DroneExchange> getAvailableDrones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Drone> dronePage = droneRepo.getAllByStateIn(List.of(DroneStatus.IDLE), pageable);
        return buildDronePageExchange(page, size, dronePage);
    }

    public Integer getBatteryLevel(Long droneId) {
        Optional<Drone> droneOptional = droneRepo.findById(droneId);
        if (droneOptional.isEmpty()) {
            throw new DroneNotFoundException(String.format(DRONE_IS_NOT_AVAILABLE_FOR_GIVEN_ID_S, droneId));
        }
        return droneOptional.get().getBatteryCapacity();
    }

    public List<MedicationExchange> getAllMedicationsLoadedByDroneId(Long id) {
        Optional<Drone> droneOptional = droneRepo.findById(id);
        if (droneOptional.isEmpty()) {
            throw new DroneNotFoundException(String.format(DRONE_IS_NOT_AVAILABLE_FOR_GIVEN_ID_S, id));
        }
        return buildMedicationExchanges(droneOptional.get().getMedications());
    }

    private boolean isWeightLimitExceeded(Drone drone, List<Medication> medications) {
        return drone.getWeightLimit() < medications.stream().mapToDouble(Medication::getWeight).sum();
    }

    private PageExchange<DroneExchange> buildDronePageExchange(int page, int size, Page<Drone> dronePage) {
        return PageExchange.<DroneExchange>builder()
                .page(page)
                .pageSize(size)
                .items(buildDroneExchanges(dronePage.getContent()))
                .totalItems(dronePage.getTotalElements())
                .totalPages(dronePage.getTotalPages()).build();
    }

    private List<DroneExchange> buildDroneExchanges(List<Drone> drones) {
        return CollectionUtils.isEmpty(drones) ? null : drones.stream().map(this::buildDroneExchange).collect(Collectors.toList());
    }

    private Drone buildDrone(DroneExchange droneExchange) {
        return Drone.builder()
                .batteryCapacity(droneExchange.getBatteryCapacity())
                .model(droneExchange.getModel())
                .serialNumber(droneExchange.getSerialNumber())
                .weightLimit(droneExchange.getWeightLimit())
                .build();
    }

    private DroneExchange buildDroneExchange(Drone drone) {
        return DroneExchange.builder()
                .id(drone.getId())
                .batteryCapacity(drone.getBatteryCapacity())
                .model(drone.getModel())
                .state(drone.getState())
                .serialNumber(drone.getSerialNumber())
                .weightLimit(drone.getWeightLimit())
                .medicationExchanges(buildMedicationExchanges(drone.getMedications()))
                .build();
    }

    private List<MedicationExchange> buildMedicationExchanges(List<Medication> medications) {
        return CollectionUtils.isEmpty(medications) ? null : medications.stream().map(this::buildMedicationExchange).collect(Collectors.toList());
    }

    private MedicationExchange buildMedicationExchange(Medication medication) {
        return MedicationExchange.builder()
                .id(medication.getId())
                .code(medication.getCode())
                .imagePath(medication.getImagePath())
                .weight(medication.getWeight())
                .name(medication.getName())
                .build();
    }
}
