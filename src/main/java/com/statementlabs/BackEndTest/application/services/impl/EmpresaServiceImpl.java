package com.statementlabs.BackEndTest.application.services.impl;

import com.statementlabs.BackEndTest.application.exceptions.NifAlreadyInUseException;
import com.statementlabs.BackEndTest.application.repositories.EmpresaRepository;
import com.statementlabs.BackEndTest.application.services.EmpresaService;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.domain.model.Empresa;
import com.statementlabs.BackEndTest.domain.model.StatusEmpresa;
import com.statementlabs.BackEndTest.domain.model.TipoEmpresa;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaResponseDto;
import com.statementlabs.BackEndTest.presentation.dtos.Empresa.EmpresaUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {
    private final EmpresaRepository empresaRepository;
    private final LogAuditoriaService logAuditoriaService;

    @Override
    public EmpresaResponseDto buscarPorNif(String nif) {
        var empresa = empresaRepository.findByNif(nif).orElse(null);
        if (empresa == null)
            return null;
        return new EmpresaResponseDto(
                empresa.getId(),
                empresa.getNome(),
                empresa.getNif(),
                empresa.getTipo(),
                empresa.getStatus(),
                empresa.getDataRegistro()
        );
    }

    @Override
    public EmpresaResponseDto buscarPorId(UUID id) {
        var empresa = empresaRepository.findById(id).orElseGet(null);
        if (empresa == null)
            return null;
        return new EmpresaResponseDto(
                empresa.getId(),
                empresa.getNome(),
                empresa.getNif(),
                empresa.getTipo(),
                empresa.getStatus(),
                empresa.getDataRegistro()
        );
    }

    @Override
    public Slice<EmpresaResponseDto> listarEmpresas(short pageNumber, short pageCapacity) {
        var pageable = PageRequest.of(pageNumber, pageCapacity);
        var empresas = empresaRepository.findAll(pageable);
        var dtoList = empresas.stream()
            .map(empresa -> new EmpresaResponseDto(
                    empresa.getId(),
                    empresa.getNome(),
                    empresa.getNif(),
                    empresa.getTipo(),
                    empresa.getStatus(),
                    empresa.getDataRegistro()
            ))
            .collect(Collectors.toList());
        return new SliceImpl<>(dtoList, pageable, empresas.hasNext());
    }

    @Transactional
    @Override
    public EmpresaResponseDto registrarEmpresa(EmpresaCreationDto requestDto) {
        if (empresaRepository.existsByNif(requestDto.nif())) {
            throw new NifAlreadyInUseException("O NIF '" + requestDto.nif() + "' já está a ser usado.");
        }

        var empresa = new Empresa();
        empresa.setNome(requestDto.nome());
        empresa.setNif(requestDto.nif());
        empresa.setTipo(requestDto.tipo());
        empresa.setStatus(StatusEmpresa.ATIVA);
        empresa.setDataRegistro(LocalDateTime.now());
        empresaRepository.save(empresa);

        logAuditoriaService.registrar(
                "Empresa",
                "EMPRESA_CRIADA",
                "sistema",
                Map.of(
                        "idEmpresa", String.valueOf(empresa.getId()),
                        "nomeEmpresa", empresa.getNome(),
                        "nif", empresa.getNif(),
                        "tipo", empresa.getTipo().toString()
                )
        );

        return new EmpresaResponseDto(
            empresa.getId(),
            empresa.getNome(),
            empresa.getNif(),
            empresa.getTipo(),
            empresa.getStatus(),
            empresa.getDataRegistro()
        );
    }

    @Transactional
    @Override
    public EmpresaResponseDto actualizarEmpresa(UUID id, EmpresaUpdateDto updateDto) {
        var empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Não existe uma empresa com o ID " + id + "."));
        empresa.setNome(updateDto.nome());
        empresa.setNif(updateDto.nif());
        empresa.setTipo(updateDto.tipo());
        empresa.setStatus(updateDto.status());
        empresaRepository.save(empresa);

        logAuditoriaService.registrar(
                "Empresa",
                "EMPRESA_ACTUALIZADA",
                "sistema",
                Map.of(
                        "idEmpresa", String.valueOf(empresa.getId()),
                        "nomeEmpresa", empresa.getNome(),
                        "nif", empresa.getNif(),
                        "tipo", empresa.getTipo().toString(),
                        "status", empresa.getStatus().toString()
                )
        );

        return new EmpresaResponseDto(
                empresa.getId(),
                empresa.getNome(),
                empresa.getNif(),
                empresa.getTipo(),
                empresa.getStatus(),
                empresa.getDataRegistro()
        );
    }


}
