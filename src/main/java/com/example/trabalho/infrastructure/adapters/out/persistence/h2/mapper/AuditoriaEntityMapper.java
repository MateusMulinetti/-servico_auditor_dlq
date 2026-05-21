package com.example.trabalho.infrastructure.adapters.out.persistence.h2.mapper;

import com.example.trabalho.core.domain.bo.ItemPedidoBO;
import com.example.trabalho.infrastructure.adapters.out.persistence.h2.entity.AuditoriaEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaEntityMapper {

    public AuditoriaEntity toEntity(ItemPedidoBO bo) {
        AuditoriaEntity entity = new AuditoriaEntity();
        entity.setPayload(bo.getRawJsonPayload());
        return entity;
    }
}