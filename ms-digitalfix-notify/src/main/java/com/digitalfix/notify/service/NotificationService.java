package com.digitalfix.notify.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void process(String payload) {
        if (payload == null || !payload.matches("Orden creada con ID: [1-9][0-9]*")) {
            throw new IllegalArgumentException("Evento de orden invalido: " + payload);
        }
        // Simulacion de notificacion: aun no se envia correo ni SMS.
    }
}
