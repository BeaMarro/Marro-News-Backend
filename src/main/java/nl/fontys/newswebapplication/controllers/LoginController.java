package nl.fontys.newswebapplication.controllers;

import jakarta.validation.Valid;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import nl.fontys.newswebapplication.domain.response.LoginResponse;
import nl.fontys.newswebapplication.domain.request.LoginRequest;
import nl.fontys.newswebapplication.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}

