package edu.eci.dosw.DOSW_Library.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String email;
    private String membershipType; // VIP, Platinum, Standard
    private LocalDate registeredDate;
}