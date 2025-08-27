package com.statementlabs.BackEndTest.application.services;

import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaResponseDto;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaUpdateDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface EmpresaService {
    EmpresaResponseDto buscarPorNif(String nif);
    EmpresaResponseDto buscarPorId(UUID id);
    Slice<EmpresaResponseDto> listarEmpresas(short pageNumber, short pageCapacity);
    EmpresaResponseDto registrarEmpresa(EmpresaCreationDto requestDto);
    EmpresaResponseDto actualizarEmpresa(UUID id, EmpresaUpdateDto updateDto);
}
