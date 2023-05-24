package com.musala.drone.repository;

import com.musala.drone.config.DroneStatus;
import com.musala.drone.model.Drone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepo extends JpaRepository<Drone, Long> {
    Page<Drone> getAllByStateIn(List<DroneStatus> states, Pageable pageable);
}
