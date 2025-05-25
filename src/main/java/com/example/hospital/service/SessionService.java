package com.example.hospital.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SessionService {
    private static final Map<String, Object> activeSessions = new ConcurrentHashMap<>();

    public String createSession(Object userDto) {
        String sessionId = UUID.randomUUID().toString();
        activeSessions.put(sessionId, userDto);
        return sessionId;
    }

    public Object getSessionUser(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void invalidateSession(String sessionId) {
        activeSessions.remove(sessionId);
    }
}
