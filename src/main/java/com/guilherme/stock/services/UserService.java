package com.guilherme.stock.services;


import com.guilherme.stock.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public String authenticate() {
        return "users";
    }
}
