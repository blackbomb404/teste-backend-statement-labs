package com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal;

import com.statementlabs.BackEndTest.domain.model.Empresa;
import com.statementlabs.BackEndTest.domain.model.StatusSolicitacaoSelo;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitacaoSeloFiscalResponseDto(
        UUID idSolicitacao,
        UUID idEmpresa,
        String produto,
        StatusSolicitacaoSelo status,
        LocalDateTime dataSolicitacao
) {}
