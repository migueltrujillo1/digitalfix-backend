package com.digitalfix.workorders.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String ordersExchange;
    private final String ordersRoutingKey;
    private final String eventsExchange;
    private final String notificationRoutingKey;
    private final String auditRoutingKey;

    public OrderEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${digitalfix.messaging.orders.exchange}") String ordersExchange,
            @Value("${digitalfix.messaging.orders.routing-key}") String ordersRoutingKey,
            @Value("${digitalfix.messaging.events.exchange}") String eventsExchange,
            @Value("${digitalfix.messaging.notifications.routing-key}") String notificationRoutingKey,
            @Value("${digitalfix.messaging.audit.routing-key}") String auditRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.ordersExchange = ordersExchange;
        this.ordersRoutingKey = ordersRoutingKey;
        this.eventsExchange = eventsExchange;
        this.notificationRoutingKey = notificationRoutingKey;
        this.auditRoutingKey = auditRoutingKey;
    }

    public void publishOrderCreated(Long orderId) {
        publish(ordersExchange, ordersRoutingKey, "Orden creada con ID: " + orderId);
        publish(eventsExchange, notificationRoutingKey, "Notificacion para orden ID: " + orderId);
        publish(eventsExchange, auditRoutingKey, "Auditoria de orden ID: " + orderId);
    }

    private void publish(String exchange, String routingKey, String payload) {
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("[RABBITMQ] Enviado exchange={} key={} payload={}",
                exchange, routingKey, payload);
    }
}
