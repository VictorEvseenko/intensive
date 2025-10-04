package ru.aston.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Test;
import ru.aston.models.UpsertUserRequest;
import ru.aston.models.UserResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class UserControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testGetUserById() throws Exception {
        UpsertUserRequest userRequest = new UpsertUserRequest("Виктор", "smk666@rambler.ru", 36);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();

        int userId = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Виктор"))
                .andExpect(jsonPath("$.email").value("smk666@rambler.ru"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UpsertUserRequest userRequest1 = new UpsertUserRequest("Виктор", "smk666@rambler.ru", 36);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest1)))
                .andExpect(status().isCreated());
        UpsertUserRequest userRequest2 = new UpsertUserRequest("Светлана", "svetl@rambler.ru", 37);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest2)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<UserResponse> users = objectMapper.readValue(responseContent, new TypeReference<>() {});
        assertEquals(2, users.size());
    }


    @Test
    void testCreateUser() throws Exception {
        UpsertUserRequest userRequest = new UpsertUserRequest("Виктор", "smk666@rambler.ru", 36);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Виктор"))
                .andExpect(jsonPath("$.email").value("smk666@rambler.ru"))
                .andExpect(jsonPath("$.age").value(36));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpsertUserRequest createRequest = new UpsertUserRequest("Виктор", "smk666@rambler.ru", 36);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        int userId = objectMapper.readTree(response).get("id").asInt();

        UpsertUserRequest updateRequest = new UpsertUserRequest("Виктор", "smk666@yandex.ru", 37);
        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Виктор"))
                .andExpect(jsonPath("$.email").value("smk666@yandex.ru"))
                .andExpect(jsonPath("$.age").value(37));
    }

    @Test
    void testDeleteUser() throws Exception {
        UpsertUserRequest userRequest = new UpsertUserRequest("Виктор", "smk666@rambler.ru", 36);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();

        int userId = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());
    }
}
