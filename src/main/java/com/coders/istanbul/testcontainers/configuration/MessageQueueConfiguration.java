package com.coders.istanbul.testcontainers.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@EnableRabbit
public class MessageQueueConfiguration implements RabbitListenerConfigurer {

    private final UserMessageQueueConfigurationProperties userMessageQueueConfigurationProperties;

    @Autowired
    public MessageQueueConfiguration(UserMessageQueueConfigurationProperties userMessageQueueConfigurationProperties) {
        this.userMessageQueueConfigurationProperties = userMessageQueueConfigurationProperties;
    }

    @Bean("userQueue")
    public Queue userQueue() {
        return new Queue(userMessageQueueConfigurationProperties.getQueueName(), true,
                false, false, userMessageQueueConfigurationProperties.getArgs());
    }

    @Bean("userExchange")
    public FanoutExchange userExchange() {
        return new FanoutExchange(userMessageQueueConfigurationProperties.getExchangeName());
    }

    @Bean
    public Binding userAccountBinding(Queue userQueue, FanoutExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange);
    }

    @Bean
    public Queue userAccountDeadLetterQueue() {
        return new Queue(userMessageQueueConfigurationProperties.getDeadLetterQueueName(), true);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}