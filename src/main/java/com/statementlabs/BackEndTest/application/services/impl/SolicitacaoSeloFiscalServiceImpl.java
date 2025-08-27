package com.statementlabs.BackEndTest.application.services.impl;

import com.statementlabs.BackEndTest.application.repositories.EmpresaRepository;
import com.statementlabs.BackEndTest.application.repositories.SeloFiscalRepository;
import com.statementlabs.BackEndTest.application.repositories.SolicitacaoSeloFiscalRepository;
import com.statementlabs.BackEndTest.application.services.LogAuditoriaService;
import com.statementlabs.BackEndTest.application.services.SolicitacaoSeloFiscalService;
import com.statementlabs.BackEndTest.domain.model.SolicitacaoSeloFiscal;
import com.statementlabs.BackEndTest.domain.model.StatusEmpresa;
import com.statementlabs.BackEndTest.domain.model.StatusSolicitacaoSelo;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalRequestDto;
import com.statementlabs.BackEndTest.presentation.dtos.SolicitacaoSeloFiscal.SolicitacaoSeloFiscalResponseDto;
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
public class SolicitacaoSeloFiscalServiceImpl implements SolicitacaoSeloFiscalService {
    private final SeloFiscalRepository seloFiscalRepository;
    private final SolicitacaoSeloFiscalRepository solicitacaoSeloFiscalRepository;
    private final EmpresaRepository empresaRepository;
    private final LogAuditoriaService logAuditoriaService;

    @Transactional
    @Override
    public SolicitacaoSeloFiscalResponseDto solicitarSelo(SolicitacaoSeloFiscalRequestDto requestDto) {
        // Validando a empresa
        var empresa = empresaRepository.findById(requestDto.idEmpresa())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));

        // Validando o estado da mesma
        if (empresa.getStatus() != StatusEmpresa.ATIVA) {
            throw new IllegalStateException("Apenas empresas activas podem solicitar selos.");
        }

        // Prosseguindo com a criação
        var solicitacao = new SolicitacaoSeloFiscal();
        solicitacao.setEmpresa(empresa);
        solicitacao.setProduto(requestDto.produto());
        solicitacao.setStatus(StatusSolicitacaoSelo.PENDENTE);
        solicitacao = solicitacaoSeloFiscalRepository.save(solicitacao);

        logAuditoriaService.registrar(
                "SolicitacaoSeloFiscal",
                "SOLICITACAO_CRIADA",
                "sistema",
                Map.of(
                    "idEmpresa", empresa.getId().toString(),
                    "nomeEmpresa", empresa.getNome(),
                    "produto", solicitacao.getProduto(),
                    "dataSolicitacao", solicitacao.getDataSolicitacao().toString()
                )
        );

        return new SolicitacaoSeloFiscalResponseDto(
                solicitacao.getId(),
                solicitacao.getEmpresa().getId(),
                solicitacao.getProduto(),
                solicitacao.getStatus(),
                solicitacao.getDataSolicitacao()
        );
    }

    @Override
    public Slice<SolicitacaoSeloFiscalResponseDto> listarSolicitacoesPorEmpresa(UUID id, short pageNumber, short pageCapacity) {
        var pageable = PageRequest.of(pageNumber, pageCapacity);
        var solicitacoes = solicitacaoSeloFiscalRepository.findByEmpresaId(id, pageable);
        var dtoList = solicitacoes.stream()
                .map(solicitacao -> new SolicitacaoSeloFiscalResponseDto(
                        solicitacao.getId(),
                        solicitacao.getEmpresa().getId(),
                        solicitacao.getProduto(),
                        solicitacao.getStatus(),
                        solicitacao.getDataSolicitacao()
                ))
                .collect(Collectors.toList());
        return new SliceImpl<>(dtoList, pageable, solicitacoes.hasNext());
    }

    @Override
    public boolean existemSelosNaoValidadaApos30Dias(UUID idEmpresa) {
        return seloFiscalRepository.existemSelosNaoValidadosApos30Dias(idEmpresa, LocalDateTime.now().minusDays(30));
    }
}
