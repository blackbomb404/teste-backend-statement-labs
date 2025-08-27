package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes_selo_fiscal")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SolicitacaoSeloFiscal extends EntidadeBase {
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Column(nullable = false)
    private String produto;

    @Column(nullable = false)
    private StatusSolicitacaoSelo status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;
}
