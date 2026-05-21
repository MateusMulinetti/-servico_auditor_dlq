package com.example.trabalho.application.ports.out.persistence.h2;

import com.example.trabalho.core.domain.bo.ItemPedidoBO;

public interface SalvamentoAuditoriaPort {
    void save(ItemPedidoBO itemPedidoBO);
}