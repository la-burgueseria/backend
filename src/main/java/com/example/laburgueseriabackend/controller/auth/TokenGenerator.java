package com.example.laburgueseriabackend.controller.auth;

import java.util.Random;

public class TokenGenerator {
    public String generateToken(){
        String token = "";
        Random random = new Random();
        int cantidadCaracteresToken = 4;
        for(int i = 0 ; i < cantidadCaracteresToken ; i++){
            token += String.valueOf(random.nextInt(10));
        }

        return token;
    }
}
