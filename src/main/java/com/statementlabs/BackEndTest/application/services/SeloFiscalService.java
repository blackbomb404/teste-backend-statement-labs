package com.statementlabs.BackEndTest.application.services;

import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloFiscalResponseDto;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalRequestDto;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalResponseDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface SeloFiscalService {
    Slice<SeloFiscalResponseDto> listarSelosPorEmpresa(UUID id, short pageNumber, short pageCapacity);
    SeloFiscalResponseDto emitirSelo(SeloCreationDto seloCreationDto);
    SeloFiscalResponseDto validarSelo(UUID idSelo);
}
