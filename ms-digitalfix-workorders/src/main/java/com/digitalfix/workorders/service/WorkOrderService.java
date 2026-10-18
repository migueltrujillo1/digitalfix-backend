
package com.digitalfix.workorders.service;

import com.digitalfix.workorders.entity.WorkOrder;
import com.digitalfix.workorders.repository.WorkOrderRepository;
import com.digitalfix.workorders.messaging.OrderEventPublisher;

import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkOrderService {

    private final WorkOrderRepository repository;
    private final OrderEventPublisher eventPublisher;

    public WorkOrderService(
            WorkOrderRepository repository,
            OrderEventPublisher eventPublisher) {

        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public WorkOrder create(WorkOrder workOrder) {

        // guardar la orden en PostgreSQL RDS
        WorkOrder saved = repository.save(workOrder);

        // publicar evento en RabbitMQ
        try {
            eventPublisher.publishOrderCreated(saved.getId());
        } catch (AmqpException e) {
            // si RabbitMQ no está disponible.
            System.err.println(
                "[RABBITMQ] No se pudo publicar el evento de la orden "
                + saved.getId() + ": " + e.getMessage()
            );
        }

        return saved;
    }

    public Optional<WorkOrder> findById(Long id) {
        return repository.findById(id);
    }

    public List<WorkOrder> findAll(String status) {

        if (status != null && !status.isBlank()) {
            return repository.findByStatus(status);
        }

        return repository.findAll();
    }

    public Optional<WorkOrder> updateStatus(Long id, String status) {

        Optional<WorkOrder> optionalWorkOrder =
                repository.findById(id);

        if (optionalWorkOrder.isEmpty()) {
            return Optional.empty();
        }

        WorkOrder workOrder = optionalWorkOrder.get();
        workOrder.setStatus(status);

        return Optional.of(repository.save(workOrder));
    }
}
