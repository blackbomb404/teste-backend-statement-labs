package com.statementlabs.BackEndTest.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "empresas")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Empresa extends EntidadeAuditavel {
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String nif;

    @Column(nullable = false)
    private TipoEmpresa tipo;

    @Column(nullable = false)
    private StatusEmpresa status;

    @Column(nullable = false)
    public LocalDateTime dataRegistro;
}
