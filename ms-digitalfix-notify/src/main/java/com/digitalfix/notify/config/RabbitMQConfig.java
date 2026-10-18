package com.digitalfix.notify.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean("ordersExchange")
    public DirectExchange ordersExchange(
            @Value("${digitalfix.messaging.orders.exchange}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean("eventsExchange")
    public TopicExchange eventsExchange(
            @Value("${digitalfix.messaging.events.exchange}") String name) {
        return new TopicExchange(name, true, false);
    }

    @Bean("ordersQueue")
    public Queue ordersQueue(
            @Value("${digitalfix.messaging.orders.queue}") String name,
            @Value("${digitalfix.messaging.orders.dead-letter-exchange}") String deadLetterExchange,
            @Value("${digitalfix.messaging.orders.dead-letter-routing-key}") String deadLetterRoutingKey) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(deadLetterExchange)
                .deadLetterRoutingKey(deadLetterRoutingKey)
                .build();
    }

    @Bean("ordersDeadLetterExchange")
    public DirectExchange ordersDeadLetterExchange(
            @Value("${digitalfix.messaging.orders.dead-letter-exchange}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean("ordersDeadLetterQueue")
    public Queue ordersDeadLetterQueue(
            @Value("${digitalfix.messaging.orders.dead-letter-queue}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Binding ordersDeadLetterBinding(
            @Qualifier("ordersDeadLetterQueue") Queue queue,
            @Qualifier("ordersDeadLetterExchange") DirectExchange exchange,
            @Value("${digitalfix.messaging.orders.dead-letter-routing-key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Binding ordersBinding(
            @Qualifier("ordersQueue") Queue queue,
            @Qualifier("ordersExchange") DirectExchange exchange,
            @Value("${digitalfix.messaging.orders.routing-key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean("notificationsQueue")
    public Queue notificationsQueue(
            @Value("${digitalfix.messaging.notifications.queue}") String name,
            @Value("${digitalfix.messaging.notifications.dead-letter-exchange}") String deadLetterExchange,
            @Value("${digitalfix.messaging.notifications.dead-letter-routing-key}") String deadLetterRoutingKey) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(deadLetterExchange)
                .deadLetterRoutingKey(deadLetterRoutingKey)
                .build();
    }

    @Bean("notificationsDeadLetterExchange")
    public DirectExchange notificationsDeadLetterExchange(
            @Value("${digitalfix.messaging.notifications.dead-letter-exchange}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean("notificationsDeadLetterQueue")
    public Queue notificationsDeadLetterQueue(
            @Value("${digitalfix.messaging.notifications.dead-letter-queue}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Binding notificationsDeadLetterBinding(
            @Qualifier("notificationsDeadLetterQueue") Queue queue,
            @Qualifier("notificationsDeadLetterExchange") DirectExchange exchange,
            @Value("${digitalfix.messaging.notifications.dead-letter-routing-key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Binding notificationsBinding(
            @Qualifier("notificationsQueue") Queue queue,
            @Qualifier("eventsExchange") TopicExchange exchange,
            @Value("${digitalfix.messaging.notifications.routing-key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean("auditQueue")
    public Queue auditQueue(
            @Value("${digitalfix.messaging.audit.queue}") String name,
            @Value("${digitalfix.messaging.audit.dead-letter-exchange}") String deadLetterExchange,
            @Value("${digitalfix.messaging.audit.dead-letter-routing-key}") String deadLetterRoutingKey) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(deadLetterExchange)
                .deadLetterRoutingKey(deadLetterRoutingKey)
                .build();
    }

    @Bean("auditDeadLetterExchange")
    public DirectExchange auditDeadLetterExchange(
            @Value("${digitalfix.messaging.audit.dead-letter-exchange}") String name) {
        return new DirectExchange(name, true, false);
    }

    @Bean("auditDeadLetterQueue")
    public Queue auditDeadLetterQueue(
            @Value("${digitalfix.messaging.audit.dead-letter-queue}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Binding auditDeadLetterBinding(
            @Qualifier("auditDeadLetterQueue") Queue queue,
            @Qualifier("auditDeadLetterExchange") DirectExchange exchange,
            @Value("${digitalfix.messaging.audit.dead-letter-routing-key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Binding auditBinding(
            @Qualifier("auditQueue") Queue queue,
            @Qualifier("eventsExchange") TopicExchange exchange,
            @Value("${digitalfix.messaging.audit.routing-key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
