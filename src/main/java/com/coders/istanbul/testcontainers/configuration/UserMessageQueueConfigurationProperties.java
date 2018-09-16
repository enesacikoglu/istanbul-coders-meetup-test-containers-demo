package com.coders.istanbul.testcontainers.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration("userMessageQueueConfigurationProperties")
@ConfigurationProperties(prefix = "mq.user.update")
public class UserMessageQueueConfigurationProperties {


    private String exchangeName;
    private String queueName;
    private String deadLetterQueueName;
    private Map<String, Object> args = new HashMap<>();

    public String getDeadLetterQueueName() {
        return deadLetterQueueName;
    }

    public void setDeadLetterQueueName(String deadLetterQueueName) {
        this.deadLetterQueueName = deadLetterQueueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(final Map<String, Object> args) {
        this.args = args;
    }

}
