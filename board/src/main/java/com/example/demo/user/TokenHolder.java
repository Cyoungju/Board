package com.example.demo.user;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenHolder {

    private final Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public void setToken(String sessionId, String jwtToken) {
        tokenMap.put(sessionId, jwtToken);
    }

    public String getToken(String sessionId) {
        return tokenMap.get(sessionId);
    }

    public void removeToken(String sessionId) {
        tokenMap.remove(sessionId);
    }
}