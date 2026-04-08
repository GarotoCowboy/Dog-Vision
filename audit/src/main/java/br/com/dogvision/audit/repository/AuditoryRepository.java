package br.com.dogvision.audit.repository;

import br.com.dogvision.audit.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditoryRepository extends JpaRepository<AuditEvent, UUID> {
}
