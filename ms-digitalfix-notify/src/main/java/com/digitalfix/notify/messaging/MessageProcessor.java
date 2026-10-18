package com.digitalfix.notify.messaging;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Component
public class MessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    private final int maxAttempts;
    private final long delayMs;

    public MessageProcessor(
            @Value("${digitalfix.messaging.retry.max-attempts:3}") int maxAttempts,
            @Value("${digitalfix.messaging.retry.delay-ms:1000}") long delayMs) {
        this.maxAttempts = Math.max(1, maxAttempts);
        this.delayMs = Math.max(0, delayMs);
    }

    public void process(String domain, Message message, Channel channel,
                        Consumer<String> action) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        log.info("[{}] Evento recibido: {}", domain, payload);

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                action.accept(payload);
                channel.basicAck(deliveryTag, false);
                log.info("[ACK][{}] Procesado en intento {}: {}", domain, attempt, payload);
                return;
            } catch (Exception exception) {
                log.error("[RETRY][{}] Intento {}/{}: {}", domain,
                        attempt, maxAttempts, exception.getMessage());

                if (attempt < maxAttempts && delayMs > 0) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        // Sin ACK: RabbitMQ reencola cuando se cierre el canal.
                        throw new IllegalStateException("Procesamiento interrumpido", interrupted);
                    }
                }
            }
        }

        // requeue=false: el mensaje rechazado se dirige al DLX de su cola.
        channel.basicNack(deliveryTag, false, false);
        log.error("[NACK -> DLQ][{}] Rechazado: {}", domain, payload);
    }
}
