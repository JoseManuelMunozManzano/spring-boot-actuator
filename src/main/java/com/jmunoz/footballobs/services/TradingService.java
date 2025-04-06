package com.jmunoz.footballobs.services;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Random;

// Esta clase maneja las peticiones de intercambio (trading)
// Si detecta que la petición tiene más de 90 órdenes pendientes,
// notificará que la aplicación no puede gestionar más peticiones. Para eso,
// usará ApplicationEventPublisher, que será inyectado al contructor.
@Service
public class TradingService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TradingService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public int getPendingOrders() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public int tradeCards(int orders) {
        if (getPendingOrders() > 90) {
            AvailabilityChangeEvent.publish(applicationEventPublisher,
                    new Exception("There are more than 90 pending orders"),
                    LivenessState.BROKEN);
        } else {
            AvailabilityChangeEvent.publish(applicationEventPublisher,
                    new Exception("working fine"),
                    LivenessState.CORRECT);
        }
        return orders;
    }
}
