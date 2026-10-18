package com.digitalfix.notify.messaging;

import com.rabbitmq.client.Channel;
import com.digitalfix.notify.service.AuditEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuditListener {

    private static final Logger log = LoggerFactory.getLogger(AuditListener.class);
    private final MessageProcessor processor;
    private final AuditEventService auditEventService;

    public AuditListener(MessageProcessor processor, AuditEventService auditEventService) {
        this.processor = processor;
        this.auditEventService = auditEventService;
    }

    @RabbitListener(queues = "${digitalfix.messaging.audit.queue}")
    public void onAudit(Message message, Channel channel) throws IOException {
        processor.process("AUDIT", message, channel, payload -> {
            if (!payload.matches("Auditoria de orden ID: [1-9][0-9]*")) {
                throw new IllegalArgumentException("Auditoria invalida: " + payload);
            }
            log.info("[AUDIT] Evento registrado en log: {}", payload);
            auditEventService.record(payload);
        });
    }
}
