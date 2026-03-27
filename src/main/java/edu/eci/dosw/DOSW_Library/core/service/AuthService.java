package edu.eci.dosw.DOSW_Library.core.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String login(String username, String password) {
        return "token";
    }
}
