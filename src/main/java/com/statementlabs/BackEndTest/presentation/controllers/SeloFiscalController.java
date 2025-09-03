package com.statementlabs.BackEndTest.presentation.controllers;

import com.statementlabs.BackEndTest.application.services.SeloFiscalService;
import com.statementlabs.BackEndTest.presentation.dtos.ErrorMessage;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloFiscalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "Listagem de selos fiscais por empresa.",
            description = "Permite listar os selos fiscais de uma empresa, a partir do UUID da mesma.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Selos listados com sucesso!"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Empresa inexistente.",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))

                    )
            }
    )
    public ResponseEntity<Slice<SeloFiscalResponseDto>> listarSelosFiscaisPorEmpresa(
            @PathVariable(name = "idEmpresa") UUID id,
            @RequestParam(defaultValue = "0") short pageNumber,
            @RequestParam(defaultValue = "5") short pageCapacity
    ) {
        var selos = seloFiscalService.listarSelosPorEmpresa(id, pageNumber, pageCapacity);
        return ResponseEntity.ok(selos);
    }

    @PostMapping
    @Operation(
            summary = "Emissão de selos fiscais.",
            description = "Permite a emissão de selos fiscais a partir do UUID de uma solicitação, requisitado no corpo da requisição.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Selo emitido com sucesso!"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Solicitação inexistente.",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Operação inválida! A solicitação deve estar em estado pendente.",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    public ResponseEntity<SeloFiscalResponseDto> emitirSelo(@Valid @RequestBody SeloCreationDto seloCreationDto) {
        var selo = seloFiscalService.emitirSelo(seloCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(selo);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Validação de Selos Fiscais.",
            description = "Permite validar selos fiscais. Os selos inicialmente encontram-se em estado PENDENTE.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Selo validado com sucesso!"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Selo inexistente.",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Operação inválida! O selo deve estar em estado pendente.",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
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
