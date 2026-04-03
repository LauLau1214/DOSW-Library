package edu.eci.dosw.DOSW_Library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldCreateBook() throws Exception {
        String json = """
            {
                "id": "BC1",
                "title": "Clean Code",
                "author": "Robert Martin",
                "copies": 3,
                "isbn": "978-0132350884",
                "publicationType": "PHYSICAL"
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldFailWhenCopiesIsZero() throws Exception {
        String json = """
            {
                "id": "BC2",
                "title": "Libro",
                "author": "Autor",
                "copies": 0,
                "isbn": "978-0132350884",
                "publicationType": "PHYSICAL"
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldGetBookById() throws Exception {
        String json = """
            {
                "id": "BC3",
                "title": "Titulo",
                "author": "Autor",
                "copies": 2,
                "isbn": "978-0132350884",
                "publicationType": "PHYSICAL"
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/books/BC3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("BC3"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailWhenBookNotFound() throws Exception {
        mockMvc.perform(get("/books/ID-QUE-NO-EXISTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldUpdateAvailability() throws Exception {
        String json = """
            {
                "id": "BC4",
                "title": "Libro",
                "author": "Autor",
                "copies": 1,
                "isbn": "978-0132350884",
                "publicationType": "PHYSICAL"
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/books/BC4/availability")
                        .param("available", "false"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldFailWhenUpdateAvailabilityBookNotFound() throws Exception {
        mockMvc.perform(put("/books/NO-EXISTE/availability")
                        .param("available", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldDeleteBook() throws Exception {
        String json = """
            {
                "id": "BC5",
                "title": "Para borrar",
                "author": "Autor",
                "copies": 1,
                "isbn": "978-0132350884",
                "publicationType": "PHYSICAL"
            }
            """;
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/books/BC5"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldFailWhenDeleteBookNotFound() throws Exception {
        mockMvc.perform(delete("/books/NO-EXISTE"))
                .andExpect(status().isNotFound());
    }
}