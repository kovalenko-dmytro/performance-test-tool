package com.gmail.apachdima.ptt.execution.context;

import lombok.Builder;
import org.springframework.amqp.core.Binding;

@Builder
public record MessageBrokerContext(
    String executionId,
    String queueName,
    String exchangeName,
    Binding binding,
    String routingKey
) {
}
