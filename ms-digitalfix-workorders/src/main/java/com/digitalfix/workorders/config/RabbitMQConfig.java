package com.digitalfix.workorders.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange ordersExchange(
            @Value("${digitalfix.messaging.orders.exchange}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean
    public TopicExchange eventsExchange(
            @Value("${digitalfix.messaging.events.exchange}") String name) {
        return new TopicExchange(name, true, false);
    }
}
