package com.guilherme.stock.controllers;

import com.guilherme.stock.dto.requests.UserLoginDTO;
import com.guilherme.stock.dto.responses.SimpleResponse;
import com.guilherme.stock.entities.UserApp;
import com.guilherme.stock.services.JwtService;
import com.guilherme.stock.services.UserService;
import jakarta.validation.Valid;
import javassist.Loader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping({"/authenticate", "/authenticate/"})
    public ResponseEntity<SimpleResponse> getUsers(@Valid @RequestBody UserLoginDTO data) {
        UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        Authentication auth =  authenticationManager.authenticate(passwordAuthenticationToken);

        UserApp loggedUser = (UserApp) auth.getPrincipal();

        Map<String, Object> claims = Map.of("email", loggedUser.getEmail(), "name", loggedUser.getName());
        String bearerToken = jwtService.generateToken(loggedUser, claims);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Access-Token", bearerToken);

        return new ResponseEntity<>(new SimpleResponse("User authenticated"), responseHeaders, HttpStatus.OK);

    }

}
