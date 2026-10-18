package com.digitalfix.notify.messaging;

import com.digitalfix.notify.service.NotificationService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderCreatedListener {

    private final MessageProcessor processor;
    private final NotificationService service;

    public OrderCreatedListener(MessageProcessor processor, NotificationService service) {
        this.processor = processor;
        this.service = service;
    }

    @RabbitListener(queues = "${digitalfix.messaging.orders.queue}")
    public void onOrderCreated(Message message, Channel channel) throws IOException {
        processor.process("ORDERS", message, channel, service::process);
    }
}
