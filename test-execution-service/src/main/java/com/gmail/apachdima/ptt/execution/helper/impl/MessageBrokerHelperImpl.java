package com.gmail.apachdima.ptt.execution.helper.impl;

import com.gmail.apachdima.ptt.common.constant.mq.MessageBrokerConstant;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponseDTO;
import com.gmail.apachdima.ptt.execution.context.MessageBrokerContext;
import com.gmail.apachdima.ptt.execution.context.TestExecutionContext;
import com.gmail.apachdima.ptt.execution.helper.MessageBrokerHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageBrokerHelperImpl implements MessageBrokerHelper {

    private final AmqpAdmin amqpAdmin;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public MessageBrokerContext init(String identifier) {
        String queueName = MessageBrokerConstant.TEST_EXECUTION_QUEUE_PREFIX.getValue() + identifier;
        String exchangeName = MessageBrokerConstant.TEST_EXECUTION_EXCHANGE_PREFIX.getValue() + identifier;
        String routingKey = MessageBrokerConstant.TEST_EXECUTION_ROUTINE_KEY.getValue() + identifier;

        Queue queue = createQueue(queueName);
        TopicExchange exchange = createExchange(exchangeName);
        Binding binding = createBinding(queue, exchange, routingKey);

        return MessageBrokerContext.builder()
            .executionId(identifier)
            .queueName(queueName)
            .exchangeName(exchangeName)
            .binding(binding)
            .routingKey(routingKey)
            .build();
    }

    @Override
    public void clear(MessageBrokerContext mbc) {
        amqpAdmin.removeBinding(mbc.binding());
        amqpAdmin.deleteQueue(mbc.queueName());
        amqpAdmin.deleteExchange(mbc.exchangeName());
    }

    @Override
    public void send(TestExecutionContext tec) {
        rabbitTemplate
            .convertAndSend(
                tec.getMessageBrokerContext().exchangeName(),
                tec.getMessageBrokerContext().routingKey(),
                TestExecutionStatusResponseDTO.builder()
                    .executionId(tec.getExecutionId())
                    .progress(tec.getProgress())
                    .status(tec.getStatus())
                    .resultUrl(tec.getResultUrl())
                    .build());
    }

    @Override
    public Object receive(String queueName) {
        return rabbitTemplate.receiveAndConvert(queueName);
    }

    private Queue createQueue(String queueName) {
        Queue queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    private TopicExchange createExchange(String exchangeName) {
        TopicExchange exchange = new TopicExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
        return exchange;
    }

    private Binding createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        amqpAdmin.declareBinding(binding);
        return binding;
    }
}
