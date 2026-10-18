package com.digitalfix.notify.messaging;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final MessageProcessor processor;

    public NotificationListener(MessageProcessor processor) {
        this.processor = processor;
    }

    @RabbitListener(queues = "${digitalfix.messaging.notifications.queue}")
    public void onNotification(Message message, Channel channel) throws IOException {
        processor.process("NOTIFICATIONS", message, channel, payload -> {
            if (!payload.matches("Notificacion para orden ID: [1-9][0-9]*")) {
                throw new IllegalArgumentException("Notificacion invalida: " + payload);
            }
            log.info("[NOTIFY] Notificacion simulada: {}", payload);
        });
    }
}
