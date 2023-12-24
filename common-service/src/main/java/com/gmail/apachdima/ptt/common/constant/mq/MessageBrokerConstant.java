package com.gmail.apachdima.ptt.common.constant.mq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessageBrokerConstant {

    TEST_EXECUTION_QUEUE_PREFIX("q."),
    TEST_EXECUTION_EXCHANGE_PREFIX("test-execution-exchange-"),
    TEST_EXECUTION_ROUTINE_KEY("test-execution-routing-key-");

    private final String value;
}
