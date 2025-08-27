package com.statementlabs.BackEndTest.application.repositories;

import com.statementlabs.BackEndTest.domain.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, UUID> {
}
