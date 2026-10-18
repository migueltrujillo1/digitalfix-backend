package com.digitalfix.rabbitadmin.service;

import java.util.Locale;
import java.util.Map;

import com.digitalfix.rabbitadmin.dto.BindingRequest;
import com.digitalfix.rabbitadmin.dto.ExchangeRequest;
import com.digitalfix.rabbitadmin.dto.QueueRequest;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final RabbitAdmin admin;
    private final String prefix;

    public RabbitAdminService(
            RabbitAdmin admin,
            @Value("${digitalfix.admin.allowed-prefix}") String prefix) {
        this.admin = admin;
        this.prefix = prefix;
    }

    private void check(String name) {
        if (name == null || !name.startsWith(prefix) || name.length() <= prefix.length()) {
            throw new IllegalArgumentException("Solo recursos de prueba con prefijo: " + prefix);
        }
    }

    public Map<String, String> createQueue(QueueRequest dto) {
        check(dto.name());
        admin.declareQueue(QueueBuilder.durable(dto.name()).build());
        return Map.of("message", "Cola declarada", "name", dto.name());
    }

    public Map<String, String> deleteQueue(String name) {
        check(name);
        if (!admin.deleteQueue(name)) {
            throw new IllegalArgumentException("Cola no encontrada: " + name);
        }
        return Map.of("message", "Cola eliminada", "name", name);
    }

    public Map<String, String> createExchange(ExchangeRequest dto) {
        check(dto.name());

        Exchange exchange = switch (dto.type().toLowerCase(Locale.ROOT)) {
            case "direct" -> new DirectExchange(dto.name(), true, false);
            case "topic" -> new TopicExchange(dto.name(), true, false);
            case "fanout" -> new FanoutExchange(dto.name(), true, false);
            default -> throw new IllegalArgumentException("Tipo no válido");
        };

        admin.declareExchange(exchange);
        return Map.of("message", "Exchange declarado", "name", dto.name());
    }

    public Map<String, String> deleteExchange(String name) {
        check(name);
        if (!admin.deleteExchange(name)) {
            throw new IllegalArgumentException("Exchange no encontrado: " + name);
        }
        return Map.of("message", "Exchange eliminado", "name", name);
    }

    public Map<String, String> createBinding(BindingRequest dto) {
        check(dto.queue());
        check(dto.exchange());

        admin.declareBinding(new Binding(
                dto.queue(),
                Binding.DestinationType.QUEUE,
                dto.exchange(),
                dto.routingKey(),
                null
        ));

        return Map.of(
                "message", "Binding declarado",
                "queue", dto.queue(),
                "exchange", dto.exchange(),
                "routingKey", dto.routingKey()
        );
    }
}
