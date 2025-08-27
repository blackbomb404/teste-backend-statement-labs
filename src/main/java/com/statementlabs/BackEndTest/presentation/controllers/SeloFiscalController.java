package com.statementlabs.BackEndTest.presentation.controllers;

import com.statementlabs.BackEndTest.application.services.SeloFiscalService;
import com.statementlabs.BackEndTest.presentation.dtos.ErrorMessage;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloFiscalResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/selos")
@Tag(name = "Selos Fiscais")
@RequiredArgsConstructor
public class SeloFiscalController {
    private final SeloFiscalService seloFiscalService;

    @GetMapping("/{idEmpresa}")
    public ResponseEntity<Slice<SeloFiscalResponseDto>> listarSelosFiscaisPorEmpresa(
            UUID idEmpresa,
            @RequestParam(defaultValue = "0") short pageNumber,
            @RequestParam(defaultValue = "5") short pageCapacity
    ) {
        var selos = seloFiscalService.listarSelosPorEmpresa(idEmpresa, pageNumber, pageCapacity);
        return ResponseEntity.ok(selos);
    }

    @PostMapping()
    public ResponseEntity<SeloFiscalResponseDto> emitirSelo(@Valid @RequestBody SeloCreationDto seloCreationDto) {
        var selo = seloFiscalService.emitirSelo(seloCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(selo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeloFiscalResponseDto> validarSelo(UUID id) {
        var selo = seloFiscalService.validarSelo(id);
        return ResponseEntity.ok(selo);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorMessage handleIllegalStateException(IllegalStateException ex) {
        return new ErrorMessage(ex.getMessage());
    }

}
