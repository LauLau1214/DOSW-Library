package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.AuthDTO;
import edu.eci.dosw.DOSW_Library.controller.dto.TokenDTO;
import edu.eci.dosw.DOSW_Library.core.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Autenticación de usuarios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login — obtener token JWT")
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthDTO authDTO) {

        String token = authService.login(
                authDTO.getUsername(),
                authDTO.getPassword()
        );

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
