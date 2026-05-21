package com.example.trabalho.infrastructure.adapters.in.sqs.mapper;

import com.example.trabalho.core.domain.bo.ItemPedidoBO;
import com.example.trabalho.core.domain.bo.RegistroFalhaBO;
import com.example.trabalho.infrastructure.adapters.in.sqs.dto.ItemSqsDTO;
import com.example.trabalho.infrastructure.adapters.in.sqs.dto.MensagemSqsDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MensagemSqsMapper {

    public ItemPedidoBO toBO(ItemSqsDTO dto, String rawJson) {
        List<RegistroFalhaBO> itemsBO = new ArrayList<>();

        if (dto.getOrderItems() != null) {
            for (MensagemSqsDTO itemDto : dto.getOrderItems()) {
                itemsBO.add(new RegistroFalhaBO(itemDto.getSku(), itemDto.getAmount()));
            }
        }

        return new ItemPedidoBO(
                dto.getZipCode(),
                dto.getCustomerId(),
                itemsBO,
                dto.getOrigin(),
                dto.getOccurredAt(),
                rawJson
        );
    }
}