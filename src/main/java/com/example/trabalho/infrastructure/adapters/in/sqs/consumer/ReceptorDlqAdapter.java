package com.example.trabalho.infrastructure.adapters.in.sqs.consumer;

import com.example.trabalho.application.ports.in.service.TratamentoFalhaPort;
import com.example.trabalho.core.domain.bo.ItemPedidoBO;
import com.example.trabalho.infrastructure.adapters.in.sqs.dto.ItemSqsDTO;
import com.example.trabalho.infrastructure.adapters.in.sqs.mapper.MensagemSqsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceptorDlqAdapter {

    private final TratamentoFalhaPort tratamentoFalhaPort;
    private final ObjectMapper objectMapper;
    private final MensagemSqsMapper mensagemSqsMapper;

    public ReceptorDlqAdapter(TratamentoFalhaPort tratamentoFalhaPort, ObjectMapper objectMapper, MensagemSqsMapper mensagemSqsMapper) {
        this.tratamentoFalhaPort = tratamentoFalhaPort;
        this.objectMapper = objectMapper;
        this.mensagemSqsMapper = mensagemSqsMapper;
    }

    @SqsListener("${queue.order-events}")
    public void receiveMessage(String messageJson) {
        try {
            System.out.println("[DLQ CONSUMER] Mensagem interceptada da fila: {" + messageJson + "}");

            ItemSqsDTO dto = objectMapper.readValue(messageJson, ItemSqsDTO.class);

            ItemPedidoBO bo = mensagemSqsMapper.toBO(dto, messageJson);
            tratamentoFalhaPort.execute(bo);

            System.out.println("[DLQ CONSUMER] Mensagem processada e salva com sucesso. Removendo da fila.");
        } catch (Exception e) {
            System.out.println("[DLQ CONSUMER] Falha crítica de persistência. A mensagem será mantida na DLQ." +  e);
            throw new RuntimeException("Erro no processamento da auditoria. Retendo mensagem.", e);
        }
    }
}