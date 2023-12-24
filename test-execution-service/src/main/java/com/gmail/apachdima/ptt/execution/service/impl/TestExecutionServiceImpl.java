package com.gmail.apachdima.ptt.execution.service.impl;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import com.gmail.apachdima.ptt.common.constant.mq.MessageBrokerConstant;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequest;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponse;
import com.gmail.apachdima.ptt.execution.service.TestExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestExecutionServiceImpl implements TestExecutionService {

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    private TestExecutionStatusResponse response;

    @Async
    @Override
    public void execute(String executionId, TestExecutionRequest request, String currentUrl, Locale locale) {
        String queueName = MessageBrokerConstant.TEST_EXECUTION_QUEUE_PREFIX.getValue() + executionId;
        String exchangeName = MessageBrokerConstant.TEST_EXECUTION_EXCHANGE_PREFIX.getValue() + executionId;
        String routingKey = MessageBrokerConstant.TEST_EXECUTION_ROUTINE_KEY.getValue() + executionId;
        try {

            Queue queue = createQueue(queueName);
            TopicExchange exchange = createExchange(exchangeName);
            createBinding(queue, exchange, routingKey);

            rabbitTemplate.convertAndSend(exchangeName, routingKey, TestExecutionStatusResponse.builder()
                .executionId(executionId).progress(0.0f).status(TestExecutionStatus.SUBMITTED).build());

            Thread.currentThread().sleep(20000l);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, TestExecutionStatusResponse.builder()
                .executionId(executionId).progress(10.0f).status(TestExecutionStatus.STARTED).build());

            Thread.currentThread().sleep(50000l);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, TestExecutionStatusResponse.builder()
                .executionId(executionId).progress(50.0f).status(TestExecutionStatus.RUNNING).build());

            Thread.currentThread().sleep(50000l);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, TestExecutionStatusResponse.builder()
                .executionId(executionId).progress(100.0f).status(TestExecutionStatus.FINISHED)
                .resultUrl(currentUrl.concat(CommonConstant.SLASH.getValue()).concat(executionId)).build());

        } catch (InterruptedException e) {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, TestExecutionStatusResponse.builder()
                .executionId(executionId).progress(100.0f).status(TestExecutionStatus.TERMINATED).build());
            throw new RuntimeException(e);
        }





        /*Path workDirectory = Paths.get(System.getProperty("user.dir"), "test-execution-directory");
        String command = "mvn io.gatling:gatling-maven-plugin:test -DbaseUrl=http://computer-database.gatling.io";
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(workDirectory.toString()));
            builder.redirectErrorStream(true);
            if (isWindows) {
                builder.command("cmd.exe", "/c", command);
            } else {
                builder.command("sh", "-c", command);
            }
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public TestExecutionStatusResponse getLatestTestExecutionStatus(String executionId) {
        String queueName = MessageBrokerConstant.TEST_EXECUTION_QUEUE_PREFIX.getValue() + executionId;
        TestExecutionStatusResponse received = (TestExecutionStatusResponse) rabbitTemplate.receiveAndConvert(queueName);
        if (received != null && received.progress() == 100.0f) {
            response = received;
        }
        return received;
    }

    @Override
    public TestExecutionStatusResponse getExecution(String executionId, Locale locale) {
        //TODO from DB
        return Objects.isNull(response) ? TestExecutionStatusResponse.builder().build() : response;
    }

    private Queue createQueue(String queueName) {
        Queue queue  = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    private TopicExchange createExchange(String exchangeName) {
        TopicExchange exchange  = new TopicExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
        return exchange;
    }

    private void createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        amqpAdmin.declareBinding(binding);
    }
}
