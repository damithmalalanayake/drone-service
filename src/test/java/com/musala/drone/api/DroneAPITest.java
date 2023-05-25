package com.musala.drone.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.musala.drone.config.APIConfig;
import com.musala.drone.model.exchange.DroneExchange;
import com.musala.drone.model.exchange.MedicationExchange;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DroneAPITest {
    public ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    MockMvc mock;

    @Test
    @Order(1)
    public void TEST_droneAPIShouldRegisterDroneAndReturn200Ok() throws Exception {
        DroneExchange droneExchange = DroneExchange.builder()
                .batteryCapacity(40)
                .model("New")
                .serialNumber("554871dfd")
                .weightLimit(500.00)
                .build();
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(20)
    public void TEST_droneAPIShouldThrowAnErrorWhenWeightLimitIsOver500() throws Exception {
        DroneExchange droneExchange = DroneExchange.builder()
                .batteryCapacity(40)
                .model("New")
                .serialNumber("554871dfd")
                .weightLimit(502.00)
                .build();
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(30)
    public void TEST_droneAPIShouldThrowAnErrorWhenBatteryCapacityIsOver100() throws Exception {
        DroneExchange droneExchange = DroneExchange.builder()
                .batteryCapacity(102)
                .model("New")
                .serialNumber("554871dfd")
                .weightLimit(500.00)
                .build();
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(31)
    public void TEST_droneAPIShouldThrowAnErrorWhenSerialNumberExceedsCharacterCount() throws Exception {
        DroneExchange droneExchange = DroneExchange.builder()
                .batteryCapacity(25)
                .model("New")
                .serialNumber("554871dfddddddddfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdddfdfdfdfdfddfdfdfdfddfdfdfdfdfdfdddfdfff")
                .weightLimit(500.00)
                .build();
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(40)
    public void TEST_availableDronesAPIShouldReturn4Drones() throws Exception {
        this.mock.perform(get(APIConfig.FINAL_URL_API_DRONE_AVAILABLE)
                        .param("page", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems", Matchers.equalTo(4)));
    }

    @Test
    @Order(50)
    public void TEST_batteryLevelForGivenDroneIdShouldReturn50() throws Exception {
        this.mock.perform(get(APIConfig.FINAL_URL_API_DRONE_BATTERY_LEVEL + "/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo(50)));
    }

    @Test
    @Order(60)
    public void TEST_batteryLevelForGivenInvalidDroneIdShouldReturn400WithErrorMessage() throws Exception {
        this.mock.perform(get(APIConfig.FINAL_URL_API_DRONE_BATTERY_LEVEL + "/8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.equalTo("Drone is not available for given id 8")));
    }

    @Test
    @Order(70)
    public void TEST_loadDroneWithMedicationsShouldReturn200WithLoadedMedications() throws Exception {
        List<MedicationExchange> medicationExchanges = List.of(MedicationExchange.builder().id(1l).build(),
                MedicationExchange.builder().id(2l).build(),
                MedicationExchange.builder().id(3l).build(),
                MedicationExchange.builder().id(4l).build());
        this.mock.perform(put(APIConfig.FINAL_URL_API_DRONE_LOAD + "/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ow.writeValueAsString(medicationExchanges))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(71)
    public void TEST_loadDroneWithMedicationsShouldReturn400WhenGivenDroneIdIsInvalid() throws Exception {
        List<MedicationExchange> medicationExchanges = List.of(MedicationExchange.builder().id(1l).build());
        this.mock.perform(put(APIConfig.FINAL_URL_API_DRONE_LOAD + "/10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ow.writeValueAsString(medicationExchanges))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.equalTo("Drone is not available for given id 10")));
    }

    @Test
    @Order(72)
    public void TEST_loadDroneWithMedicationsShouldReturn400WhenDroneIsInLoadingState() throws Exception {
        List<MedicationExchange> medicationExchanges = List.of(MedicationExchange.builder().id(1l).build());
        this.mock.perform(put(APIConfig.FINAL_URL_API_DRONE_LOAD + "/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ow.writeValueAsString(medicationExchanges))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.equalTo("Drone should be in IDLE state, But it's in LOADING")));
    }

    @Test
    @Order(73)
    public void TEST_loadDroneWithMedicationsShouldReturn400WhenBatteryCapacityIsLessThan25() throws Exception {
        List<MedicationExchange> medicationExchanges = List.of(MedicationExchange.builder().id(1l).build());
        this.mock.perform(put(APIConfig.FINAL_URL_API_DRONE_LOAD + "/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ow.writeValueAsString(medicationExchanges))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.equalTo("Drone battery level should be more than 25, But it has only 20")));
    }

    @Test
    @Order(74)
    public void TEST_loadDroneWithMedicationsShouldReturn400WhenMedicationIsNotExistForGivenId() throws Exception {
        List<MedicationExchange> medicationExchanges = List.of(MedicationExchange.builder().id(1l).build(),
                MedicationExchange.builder().id(2l).build(),
                MedicationExchange.builder().id(3l).build(),
                MedicationExchange.builder().id(10l).build());
        this.mock.perform(put(APIConfig.FINAL_URL_API_DRONE_LOAD + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ow.writeValueAsString(medicationExchanges))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.equalTo("Medication is not available for given ids")));
    }

    @Test
    @Order(75)
    public void TEST_loadDroneWithMedicationsShouldReturn400WhenWeightLimitIsExceeded() throws Exception {
        List<MedicationExchange> medicationExchanges = List.of(MedicationExchange.builder().id(1l).build(),
                MedicationExchange.builder().id(2l).build(),
                MedicationExchange.builder().id(3l).build(),
                MedicationExchange.builder().id(4l).build(),
                MedicationExchange.builder().id(5l).build());
        this.mock.perform(put(APIConfig.FINAL_URL_API_DRONE_LOAD + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ow.writeValueAsString(medicationExchanges))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.equalTo("Weight limit exceeded with provided medications!")));
    }
}
