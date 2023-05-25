package com.musala.drone.repository;

import com.musala.drone.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepo extends JpaRepository<Medication, Long> {

}
