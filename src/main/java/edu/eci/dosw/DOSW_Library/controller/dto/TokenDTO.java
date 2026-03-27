package edu.eci.dosw.DOSW_Library.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String role;
    private String userId;

    public TokenDTO(String token) {
        this.token = token;
    }
}
