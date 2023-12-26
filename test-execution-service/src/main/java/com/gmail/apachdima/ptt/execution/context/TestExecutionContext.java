package com.gmail.apachdima.ptt.execution.context;

import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TestExecutionContext {

    private String executionId;
    private float progress;
    private TestExecutionStatus status;
    private String resultUrl;
    private MessageBrokerContext messageBrokerContext;
}
