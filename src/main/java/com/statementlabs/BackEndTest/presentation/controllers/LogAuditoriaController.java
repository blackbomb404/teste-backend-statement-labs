package com.statementlabs.BackEndTest.presentation.controllers;

import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.domain.model.LogAuditoria;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/logs")
@Tag(name = "Logs")
@RequiredArgsConstructor
public class LogAuditoriaController {
    private final LogAuditoriaService logAuditoriaService;

    @GetMapping
    public ResponseEntity<Slice<LogAuditoria>> listarLogs(
            @RequestParam(defaultValue = "0") short pageNumber,
            @RequestParam(defaultValue = "5") short pageCapacity
    ) {
        var logs = logAuditoriaService.listarLogs(pageNumber, pageCapacity);
        return ResponseEntity.ok(logs);
    }
}
