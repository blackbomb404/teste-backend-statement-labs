package com.statementlabs.BackEndTest.application.services;

import com.statementlabs.BackEndTest.domain.model.SolicitacaoSeloFiscal;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalRequestDto;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalResponseDto;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SolicitacaoSeloFiscalService {
    SolicitacaoSeloFiscalResponseDto solicitarSelo(SolicitacaoSeloFiscalRequestDto requestDto);
    Slice<SolicitacaoSeloFiscalResponseDto> listarSolicitacoesPorEmpresa(UUID id, short pageNumber, short pageCapacity);
    boolean existemSelosNaoValidadaApos30Dias(UUID idEmpresa);
}
