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
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateBook() throws Exception {
        String json = """
            {
                "id": "BC1",
                "title": "Clean Code",
                "author": "Robert Martin",
                "copies": 3
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailWhenCopiesIsZero() throws Exception {
        String json = """
            {
                "id": "BC2",
                "title": "Libro",
                "author": "Autor",
                "copies": 0
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookById() throws Exception {
        String json = """
            {
                "id": "BC3",
                "title": "Titulo",
                "author": "Autor",
                "copies": 2
            }
            """;
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/books/BC3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("BC3"));
    }

    @Test
    void shouldFailWhenBookNotFound() throws Exception {
        mockMvc.perform(get("/books/ID-QUE-NO-EXISTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateAvailability() throws Exception {
        String json = """
            {
                "id": "BC4",
                "title": "Libro",
                "author": "Autor",
                "copies": 1
            }
            """;
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(put("/books/BC4/availability")
                        .param("available", "false"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenUpdateAvailabilityBookNotFound() throws Exception {
        mockMvc.perform(put("/books/NO-EXISTE/availability")
                        .param("available", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        String json = """
            {
                "id": "BC5",
                "title": "Para borrar",
                "author": "Autor",
                "copies": 1
            }
            """;
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(delete("/books/BC5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailWhenDeleteBookNotFound() throws Exception {
        mockMvc.perform(delete("/books/NO-EXISTE"))
                .andExpect(status().isBadRequest());
    }
}