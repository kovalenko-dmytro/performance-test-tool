package com.gmail.apachdima.ptt.execution.helper;

import com.gmail.apachdima.ptt.execution.context.MessageBrokerContext;
import com.gmail.apachdima.ptt.execution.context.TestExecutionContext;

public interface MessageBrokerHelper {

    MessageBrokerContext init(String identifier);
    void clear(MessageBrokerContext mbc);
    void send(TestExecutionContext tec);
    Object receive(String queueName);
}
