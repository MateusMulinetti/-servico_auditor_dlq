package com.example.trabalho.infrastructure.adapters.out.persistence.h2.repository;


import com.example.trabalho.application.ports.out.persistence.h2.SalvamentoAuditoriaPort;
import com.example.trabalho.core.domain.bo.ItemPedidoBO;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.entity.AuditoriaEntity;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.jpa.AuditoriaJPAEntity;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.mapper.AuditoriaEntityMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class SalvamentoAuditoriaPortAdapter implements SalvamentoAuditoriaPort {

    private final AuditoriaJPAEntity jpaRepository;
    private final AuditoriaEntityMapper auditoriaEntityMapper;
    private final String dlqName;

    public SalvamentoAuditoriaPortAdapter(AuditoriaJPAEntity jpaRepository,
                                          AuditoriaEntityMapper auditoriaEntityMapper,
                                          @Value("${queue.order-events}") String dlqName) {
        this.jpaRepository = jpaRepository;
        this.auditoriaEntityMapper = auditoriaEntityMapper;
        this.dlqName = dlqName;
    }

    @Override
    public void save(ItemPedidoBO itemPedidoBO) {
        AuditoriaEntity entity = auditoriaEntityMapper.toEntity(itemPedidoBO);

        entity.setErrorId(UUID.randomUUID().toString());
        entity.setQueueName(this.dlqName);
        entity.setTimestamp(OffsetDateTime.now().toString());
        entity.setStatus("PENDING_ANALYSIS");

        entity.setSeverity(itemPedidoBO.calculateSeverity());

        jpaRepository.save(entity);
    }
}