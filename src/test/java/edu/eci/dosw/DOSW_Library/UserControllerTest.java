package edu.eci.dosw.DOSW_Library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration"
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterUser() throws Exception {
        String json = """
            {
                "id": "U1",
                "name": "Maria",
                "username": "maria01",
                "password": "pass123",
                "role": "USER",
                "email": "maria@email.com",
                "membershipType": "Standard",
                "registeredDate": "2024-01-15"
            }
            """;
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailWhenPasswordIsNull() throws Exception {
        String json = """
            {
                "id": "U-NOPWD",
                "name": "Sin Password",
                "username": "sinpwd",
                "role": "USER"
            }
            """;
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldGetUserById() throws Exception {
        String json = """
            {
                "id": "U2",
                "name": "Carlos",
                "username": "carlos02",
                "password": "pass123",
                "role": "USER",
                "email": "carlos@email.com",
                "membershipType": "Standard",
                "registeredDate": "2024-01-15"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/users/U2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("U2"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldFailWhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/ID-QUE-NO-EXISTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldUpdateUser() throws Exception {
        String json = """
            {
                "id": "U3",
                "name": "Pedro",
                "username": "pedro03",
                "password": "pass123",
                "role": "USER",
                "email": "pedro@email.com",
                "membershipType": "Standard",
                "registeredDate": "2024-01-15"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        String updated = """
            {
                "id": "U3",
                "name": "Pedro Actualizado",
                "username": "pedro03",
                "password": "pass123",
                "role": "USER",
                "email": "pedro@email.com",
                "membershipType": "VIP",
                "registeredDate": "2024-01-15"
            }
            """;
        mockMvc.perform(put("/users/U3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldFailWhenUpdateUserNotFound() throws Exception {
        String updated = """
            {
                "id": "NO-EXISTE",
                "name": "Nadie",
                "username": "nadie",
                "password": "pass123",
                "role": "USER"
            }
            """;
        mockMvc.perform(put("/users/NO-EXISTE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldDeleteUser() throws Exception {
        String json = """
            {
                "id": "U4",
                "name": "Ana",
                "username": "ana04",
                "password": "pass123",
                "role": "USER",
                "email": "ana@email.com",
                "membershipType": "Standard",
                "registeredDate": "2024-01-15"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(delete("/users/U4"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldFailWhenDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/NO-EXISTE"))
                .andExpect(status().isNotFound());
    }
}