package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.SQLType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "logs_auditoria")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoria extends EntidadeAuditavel {
    //@Column(columnDefinition = "VARCHAR(20) NOT NULL CHECK (entidade IN 'SeloFiscal', 'Empresa')")
    @Column(nullable = false)
    private String entidade;

    //@Column(columnDefinition = "VARCHAR(60) NOT NULL CHECK (accao IN 'VALIDACAO_REALIZADA', 'SOLICITACAO_EMITIDA')")
    @Column(nullable = false)
    private String accao;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> detalhes = new HashMap<>();
}
