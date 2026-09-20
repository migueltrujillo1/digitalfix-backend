package ev1_backend.service;

import ev1_backend.entity.WorkOrder;
import ev1_backend.repository.WorkOrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkOrderService {

    private final WorkOrderRepository repository;

    public WorkOrderService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    public WorkOrder create(WorkOrder workOrder) {
        return repository.save(workOrder);
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

        Optional<WorkOrder> optionalWorkOrder = repository.findById(id);

        if (optionalWorkOrder.isEmpty()) {
            return Optional.empty();
        }

        WorkOrder workOrder = optionalWorkOrder.get();
        workOrder.setStatus(status);

        return Optional.of(repository.save(workOrder));
    }
}