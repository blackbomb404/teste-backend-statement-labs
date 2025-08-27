package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}