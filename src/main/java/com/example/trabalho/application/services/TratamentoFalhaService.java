package com.example.trabalho.application.services;


import com.example.trabalho.application.ports.in.service.TratamentoFalhaPort;
import com.example.trabalho.application.ports.out.persistence.h2.SalvamentoAuditoriaPort;
import com.example.trabalho.core.domain.bo.ItemPedidoBO;
import org.springframework.stereotype.Service;

@Service
public class TratamentoFalhaService implements TratamentoFalhaPort {

    private final SalvamentoAuditoriaPort salvamentoAuditoriaPort;

    public TratamentoFalhaService(SalvamentoAuditoriaPort salvamentoAuditoriaPort) {
        this.salvamentoAuditoriaPort = salvamentoAuditoriaPort;
    }

    @Override
    public void execute(ItemPedidoBO itemPedidoBO) {
        this.salvamentoAuditoriaPort.save(itemPedidoBO);
    }
}