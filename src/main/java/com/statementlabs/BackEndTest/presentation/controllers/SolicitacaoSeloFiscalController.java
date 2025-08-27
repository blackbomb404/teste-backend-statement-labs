package com.statementlabs.BackEndTest.presentation.controllers;

import com.statementlabs.BackEndTest.application.exceptions.NifAlreadyInUseException;
import com.statementlabs.BackEndTest.application.repositories.SolicitacaoSeloFiscalRepository;
import com.statementlabs.BackEndTest.application.services.SeloFiscalService;
import com.statementlabs.BackEndTest.application.services.SolicitacaoSeloFiscalService;
import com.statementlabs.BackEndTest.domain.model.SolicitacaoSeloFiscal;
import com.statementlabs.BackEndTest.presentation.dtos.ErrorMessage;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalRequestDto;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/solicitacoes")
@Tag(name = "Solicitações de Selo Fiscal")
@RequiredArgsConstructor
public class SolicitacaoSeloFiscalController {
    private final SolicitacaoSeloFiscalService solicitacaoSeloFiscalService;

    @PostMapping
    public ResponseEntity<SolicitacaoSeloFiscalResponseDto> solicitarSelo(
            @Valid @RequestBody SolicitacaoSeloFiscalRequestDto requestDto
    ) {
        var solicitacao = solicitacaoSeloFiscalService.solicitarSelo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacao);
    }

    @GetMapping("/{idEmpresa}")
    public ResponseEntity<Slice<SolicitacaoSeloFiscalResponseDto>> listarSolicitacoesPorEmpresa(
            UUID idEmpresa,
            @RequestParam(defaultValue = "0") short pageNumber,
            @RequestParam(defaultValue = "5") short pageCapacity
    ) {
        var solicitacoes = solicitacaoSeloFiscalService.listarSolicitacoesPorEmpresa(idEmpresa, pageNumber, pageCapacity);
        return ResponseEntity.ok(solicitacoes);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalStateException.class, EntityNotFoundException.class})
    public ErrorMessage handleIllegalState(IllegalStateException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
