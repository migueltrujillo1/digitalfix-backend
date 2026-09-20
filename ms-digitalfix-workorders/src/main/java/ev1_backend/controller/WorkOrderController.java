package ev1_backend.controller;

import ev1_backend.entity.WorkOrder;
import ev1_backend.service.WorkOrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @PostMapping
    public ResponseEntity<WorkOrder> create(
            @RequestBody WorkOrder workOrder) {

        WorkOrder created = service.create(workOrder);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @GetMapping("/{id}")
    public ResponseEntity<WorkOrder> getById(
            @PathVariable Long id) {

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @GetMapping
    public ResponseEntity<List<WorkOrder>> getAll(
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(
                service.findAll(status)
        );
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @PutMapping("/{id}/status")
    public ResponseEntity<WorkOrder> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusRequest request) {

        return service.updateStatus(id, request.status())
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    public record StatusRequest(String status) {}
}