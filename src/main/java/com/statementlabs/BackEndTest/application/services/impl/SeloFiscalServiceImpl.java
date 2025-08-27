package com.statementlabs.BackEndTest.application.services.impl;

import com.statementlabs.BackEndTest.application.repositories.EmpresaRepository;
import com.statementlabs.BackEndTest.application.repositories.SeloFiscalRepository;
import com.statementlabs.BackEndTest.application.repositories.SolicitacaoSeloFiscalRepository;
import com.statementlabs.BackEndTest.application.services.CodigoSeloService;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.application.services.SeloFiscalService;
import com.statementlabs.BackEndTest.domain.model.*;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloCreationDto;
import com.statementlabs.BackEndTest.presentation.dtos.SeloFiscal.SeloFiscalResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeloFiscalServiceImpl implements SeloFiscalService {
    private final SeloFiscalRepository seloFiscalRepository;
    private final SolicitacaoSeloFiscalRepository solicitacaoSeloFiscalRepository;
    private final EmpresaRepository empresaRepository;
    private final LogAuditoriaService logAuditoriaService;
    private final CodigoSeloService codigoSeloService;

    @Transactional
    @Override
    public SeloFiscalResponseDto emitirSelo(SeloCreationDto seloCreationDto) {
        var solicitacao = solicitacaoSeloFiscalRepository.findById(seloCreationDto.idSolicitacao())
                .orElseThrow(() -> new EntityNotFoundException("Não existe uma solicitação com o ID " + seloCreationDto.idSolicitacao() + "."));

        if (solicitacao.getStatus() != StatusSolicitacaoSelo.PENDENTE) {
            throw new IllegalStateException(
                    String.format("Esta solicitação já foi %s! Não é possível repetir a operação.", solicitacao.getStatus().toString().toLowerCase()));
        }
        solicitacao.setStatus(StatusSolicitacaoSelo.APROVADA);
        solicitacaoSeloFiscalRepository.save(solicitacao);

        var seloFiscal = new SeloFiscal();
        seloFiscal.setCodigo(codigoSeloService.gerarCodigo());
        seloFiscal.setEmpresa(solicitacao.getEmpresa());
        seloFiscal.setProduto(solicitacao.getProduto());
        seloFiscal.setEstado(EstadoSeloFiscal.EMITIDO);
        seloFiscal = seloFiscalRepository.save(seloFiscal);

        logAuditoriaService.registrar(
                "SeloFiscal",
                "SELO_EMITIDO",
                "sistema",
                Map.of(
                        "idSolicitacao", seloCreationDto.idSolicitacao().toString(),
                        "idSelo", String.valueOf(seloFiscal.getId()),
                        "codigoSelo", seloFiscal.getCodigo(),
                        "nomeEmpresa", solicitacao.getEmpresa().getNome(),
                        "produto", solicitacao.getProduto()
                )
        );

        return new SeloFiscalResponseDto(
                seloFiscal.getId(),
                seloFiscal.getCodigo(),
                seloFiscal.getEmpresa().getId(),
                seloFiscal.getEmpresa().getNome(),
                seloFiscal.getProduto(),
                seloFiscal.getDataEmissao(),
                seloFiscal.getEstado()
        );
    }

    @Transactional
    @Override
    public SeloFiscalResponseDto validarSelo(UUID idSelo) {
        var selo = seloFiscalRepository.findById(idSelo)
                .orElseThrow(() -> new EntityNotFoundException("Não existe um selo fiscal com o ID " + idSelo + "."));
        
        if (selo.getEstado() == EstadoSeloFiscal.VALIDADO) {
            throw new IllegalStateException("Este selo já foi validado! Não é possível repetir a operação.");
        }
        selo.setEstado(EstadoSeloFiscal.VALIDADO);
        seloFiscalRepository.save(selo);

        logAuditoriaService.registrar(
                "SeloFiscal",
                "SELO_VALIDADO",
                "sistema",
                Map.of(
                        "idSelo", selo.getId().toString(),
                        "nomeEmpresa", selo.getEmpresa().getNome(),
                        "produto", selo.getProduto()
                )
        );

        return new SeloFiscalResponseDto(
                selo.getId(),
                selo.getCodigo(),
                selo.getEmpresa().getId(),
                selo.getEmpresa().getNome(),
                selo.getProduto(),
                selo.getDataEmissao(),
                selo.getEstado()
        );
    }

    @Override
    public Slice<SeloFiscalResponseDto> listarSelosPorEmpresa(UUID id, short pageNumber, short pageCapacity) {
        var pageable = PageRequest.of(pageNumber, pageCapacity);
        var selos = seloFiscalRepository.findByEmpresaId(id, pageable);
        var dtoList = selos.stream()
            .map(selo -> new SeloFiscalResponseDto(
                selo.getId(),
                selo.getCodigo(),
                selo.getEmpresa().getId(),
                selo.getEmpresa().getNome(),
                selo.getProduto(),
                selo.getDataEmissao(),
                selo.getEstado()
            ))
            .collect(Collectors.toList());
        return new SliceImpl<>(dtoList, pageable, selos.hasNext());
    }
}
