package com.musala.drone.repository;

import com.musala.drone.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepo extends JpaRepository<Drone, Long> {
}
