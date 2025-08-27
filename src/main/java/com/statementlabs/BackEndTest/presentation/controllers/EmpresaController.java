package com.statementlabs.BackEndTest.presentation.controllers;

import com.statementlabs.BackEndTest.application.exceptions.NifAlreadyInUseException;
import com.statementlabs.BackEndTest.application.services.EmpresaService;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaResponseDto;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaUpdateDto;
import com.statementlabs.BackEndTest.presentation.dtos.ErrorMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/empresas")
@Tag(name = "Empresas")
@RequiredArgsConstructor
public class EmpresaController {
    private final EmpresaService empresaService;

    @GetMapping("nif/{nif}")
    public ResponseEntity<EmpresaResponseDto> buscarPorNif(@PathVariable String nif) {
        var empresa = empresaService.buscarPorNif(nif);
        if (empresa == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(empresa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDto> buscarPorId(@PathVariable UUID id) {
        var empresa = empresaService.buscarPorId(id);
        if (empresa == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(empresa);
    }

    @GetMapping
    public ResponseEntity<Slice<EmpresaResponseDto>> listarEmpresas(
            @RequestParam(defaultValue = "0") short pageNumber,
            @RequestParam(defaultValue = "5") short pageCapacity
    ) {
        var empresas = empresaService.listarEmpresas(pageNumber, pageCapacity);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDto> registrarEmpresa(@Valid @RequestBody EmpresaCreationDto requestDto) {
        var novaEmpresa = empresaService.registrarEmpresa(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDto> actualizarEmpresa(UUID id, @Valid @RequestBody EmpresaUpdateDto updateDto) {
        var empresaActualizada = empresaService.actualizarEmpresa(id, updateDto);
        return ResponseEntity.ok(empresaActualizada);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            NifAlreadyInUseException.class,
            IllegalStateException.class,
            EntityNotFoundException.class
    })
    public ErrorMessage handleNifAlreadyInUseAndIllegalState(RuntimeException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, ArrayList<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, ArrayList<String>>();
        errors.put("erros", new ArrayList<>());
        ex.getBindingResult().getAllErrors().forEach((error) ->
            errors.get("erros").add(error.getDefaultMessage())
        );
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorMessage handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        return new ErrorMessage("O tipo ou status da empresa é inválido.");
    }
}
