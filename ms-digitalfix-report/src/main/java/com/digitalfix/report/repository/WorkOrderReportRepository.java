package com.digitalfix.report.repository;

import com.digitalfix.report.entity.WorkOrderReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkOrderReportRepository extends JpaRepository<WorkOrderReportView, Long> {
    List<WorkOrderReportView> findByCreatedAtGreaterThanEqual(LocalDateTime from);
}
