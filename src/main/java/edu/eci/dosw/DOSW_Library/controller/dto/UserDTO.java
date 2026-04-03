package edu.eci.dosw.DOSW_Library.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String password;
    private String role;

    // Campos nuevos
    private String email;
    private String membershipType; // VIP, Platinum, Standard
    private LocalDate registeredDate;
}