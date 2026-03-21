package edu.eci.dosw.DOSW_Library;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import org.junit.jupiter.api.BeforeEach;
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
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Registrar usuario
        String userJson = """
            {
                "id": "U-LOAN",
                "name": "Test User"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        String bookJson = """
            {
                "id": "B-LOAN",
                "title": "Libro Loan",
                "author": "Autor",
                "copies": 2
            }
            """;
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson));
    }

    @Test
    void shouldCreateLoan() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("B-LOAN");
        loanDTO.setUserId("U-LOAN");

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value("B-LOAN"))
                .andExpect(jsonPath("$.userId").value("U-LOAN"));
    }

    @Test
    void shouldFailWhenBookIdIsNull() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId(null);
        loanDTO.setUserId("U-LOAN");

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenUserIdIsNull() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("B-LOAN");
        loanDTO.setUserId(null);

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenUserNotFound() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("B-LOAN");
        loanDTO.setUserId("USUARIO-INEXISTENTE");

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailWhenBookNotFound() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("LIBRO-INEXISTENTE");
        loanDTO.setUserId("U-LOAN");

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isBadRequest()); // RuntimeException del BookService
    }

    @Test
    void shouldReturnBook() throws Exception {
        // Primero creamos el préstamo
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("B-LOAN");
        loanDTO.setUserId("U-LOAN");

        String response = mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        LoanDTO createdLoan = objectMapper.readValue(response, LoanDTO.class);
        String loanId = createdLoan.getLoanId();

        mockMvc.perform(put("/loans/return/" + loanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(loanId));
    }

    @Test
    void shouldFailWhenLoanNotFound() throws Exception {
        mockMvc.perform(put("/loans/return/ID-INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllLoans() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk());
    }
}