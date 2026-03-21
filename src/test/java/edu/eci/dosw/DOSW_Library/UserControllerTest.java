package edu.eci.dosw.DOSW_Library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterUser() throws Exception {
        String json = """
            {
                "id": "U1",
                "name": "Maria"
            }
            """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailWhenUserIdIsEmpty() throws Exception {
        String json = """
            {
                "id": "",
                "name": "Maria"
            }
            """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenUserIdIsNull() throws Exception {
        String json = """
            {
                "name": "Maria"
            }
            """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Primero lo registramos
        String json = """
            {
                "id": "U2",
                "name": "Carlos"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Luego lo buscamos
        mockMvc.perform(get("/users/U2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("U2"))
                .andExpect(jsonPath("$.name").value("Carlos"));
    }

    @Test
    void shouldFailWhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/ID-QUE-NO-EXISTE"))
                .andExpect(status().isNotFound()); // UserNotFoundException -> 404
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // Registramos primero
        String json = """
            {
                "id": "U3",
                "name": "Pedro"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Actualizamos
        String updated = """
            {
                "id": "U3",
                "name": "Pedro Actualizado"
            }
            """;
        mockMvc.perform(put("/users/U3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenUpdateUserNotFound() throws Exception {
        String updated = """
            {
                "id": "NO-EXISTE",
                "name": "Nadie"
            }
            """;
        mockMvc.perform(put("/users/NO-EXISTE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        String json = """
            {
                "id": "U4",
                "name": "Ana"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(delete("/users/U4"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailWhenDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/NO-EXISTE"))
                .andExpect(status().isNotFound());
    }
}