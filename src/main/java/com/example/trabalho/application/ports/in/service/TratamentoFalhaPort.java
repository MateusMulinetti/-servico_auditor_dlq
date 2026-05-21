package com.example.trabalho.application.ports.in.service;

import com.example.trabalho.core.domain.bo.ItemPedidoBO;

public interface TratamentoFalhaPort {
    void execute(ItemPedidoBO itemPedidoBO);
}