package com.coders.istanbul.testcontainers.consumer;

import com.coders.istanbul.testcontainers.UserService;
import com.coders.istanbul.testcontainers.model.dto.UserQueueDto;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = UserMessageQueueConsumerIT.Initializer.class)
public class UserMessageQueueConsumerIT {


    @TestConfiguration
    public static class Config {
        @Bean
        public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
                                             Jackson2JsonMessageConverter producerJackson2MessageConverter) {
            final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(producerJackson2MessageConverter);
            return rabbitTemplate;
        }

        @Bean
        public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
            return new Jackson2JsonMessageConverter();
        }
    }


    @ClassRule
    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @Value("${mq.user.update.queue-name}")
    private String queueName;

    @Value("${mq.user.update.dead-letter-queue-name}")
    private String deadLetterQueue;

    @Value("${mq.user.update.exchange-name}")
    private String exchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private UserService userService;


    @Test
    public void it_should_consume_message_properly() {
        //Given
        UserQueueDto userQueueDto =
                new UserQueueDto(1L, "enes.acikoglu@gmail.com");


        //When
        rabbitTemplate.convertAndSend(exchange, userQueueDto,
                postProcessor -> {
                    postProcessor.getMessageProperties().setContentType("application/json");
                    return postProcessor;
                });

        //Then
        verify(userService, timeout(1000)).updateUserEmail(argThat(
                (UserQueueDto userQueueMessage) ->
                        userQueueMessage.getEmail().equals("enes.acikoglu@gmail.com")
        ));
    }


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672),
                    "spring.rabbitmq.username=" + "guest",
                    "spring.rabbitmq.password=" + "guest",
                    "spring.rabbitmq.virtual-host=" + "/"
            );
            values.applyTo(configurableApplicationContext);
        }

    }

}