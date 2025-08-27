package com.statementlabs.BackEndTest.domain.model;

import com.statementlabs.BackEndTest.infraestructure.config.DatePrefixedSequenceIdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "selos_fiscais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SeloFiscal extends EntidadeBase {
    @Column(nullable = false, unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Column(nullable = false, length = 100)
    private String produto;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime dataEmissao;

    @Column(nullable = false)
    private EstadoSeloFiscal estado;
}
