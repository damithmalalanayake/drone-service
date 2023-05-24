package com.musala.drone.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.musala.drone.config.APIConfig;
import com.musala.drone.model.exchange.DroneExchange;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DroneAPITest {
    public ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    MockMvc mock;

    @Test
    public void TEST_droneAPIShouldRegisterDroneAndReturn200Ok() throws Exception {
        DroneExchange droneExchange = new DroneExchange();
        droneExchange.setBatteryCapacity(40);
        droneExchange.setModel("New");
        droneExchange.setSerialNumber("554871dfd");
        droneExchange.setWeightLimit(500.00);
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void TEST_droneAPIShouldThrowAnErrorWhenWeightLimitIsOver500() throws Exception {
        DroneExchange droneExchange = new DroneExchange();
        droneExchange.setBatteryCapacity(40);
        droneExchange.setModel("New");
        droneExchange.setSerialNumber("554871dfd");
        droneExchange.setWeightLimit(502.00);
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void TEST_droneAPIShouldThrowAnErrorWhenBatteryCapacityIsOver100() throws Exception {
        DroneExchange droneExchange = new DroneExchange();
        droneExchange.setBatteryCapacity(102);
        droneExchange.setModel("New");
        droneExchange.setSerialNumber("554871dfd");
        droneExchange.setWeightLimit(500.00);
        this.mock.perform(post(APIConfig.API_DRONE)
                        .content(ow.writeValueAsString(droneExchange))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
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
}
