package edu.eci.dosw.DOSW_Library;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import org.junit.jupiter.api.BeforeEach;
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
        "spring.profiles.active=relational",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration"
})
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        String userJson = """
        {
            "id": "U-LOAN",
            "name": "Test User",
            "username": "testuser",
            "password": "pass123",
            "role": "USER",
            "email": "test@email.com",
            "membershipType": "Standard",
            "registeredDate": "2024-01-15"
        }
        """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        String bookJson = """
        {
            "id": "B-LOAN",
            "title": "Libro Loan",
            "author": "Autor Loan",
            "copies": 2,
            "isbn": "978-0000000001",
            "publicationType": "BOOK",
            "language": "Spanish",
            "publisher": "Editorial"
        }
        """;
        mockMvc.perform(post("/books")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("admin").roles("LIBRARIAN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldCreateLoan() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("B-LOAN");
        loanDTO.setUserId("U-LOAN");

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "USER")
    void shouldFailWhenBookNotFound() throws Exception {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId("LIBRO-INEXISTENTE");
        loanDTO.setUserId("U-LOAN");

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBook() throws Exception {
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
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailWhenLoanNotFound() throws Exception {
        mockMvc.perform(put("/loans/return/ID-INEXISTENTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetAllLoans() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk());
    }
}