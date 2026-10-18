package com.digitalfix.notify.service;

import com.digitalfix.notify.entity.AuditEvent;
import com.digitalfix.notify.repository.AuditEventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuditEventService {

    private static final Pattern ORDER_ID = Pattern.compile("^Auditoria de orden ID: ([1-9][0-9]*)$");
    private final AuditEventRepository repository;

    public AuditEventService(AuditEventRepository repository) {
        this.repository = repository;
    }

    public AuditEvent record(String payload) {
        Matcher matcher = ORDER_ID.matcher(payload);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Evento de auditoría inválido");
        }
        return repository.save(new AuditEvent(Long.parseLong(matcher.group(1)), payload));
    }

    public List<AuditEvent> latest(int limit) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }
}
