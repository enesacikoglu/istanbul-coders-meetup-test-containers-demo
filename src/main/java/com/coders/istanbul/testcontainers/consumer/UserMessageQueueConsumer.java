package com.coders.istanbul.testcontainers.consumer;


import com.coders.istanbul.testcontainers.UserService;
import com.coders.istanbul.testcontainers.domain.User;
import com.coders.istanbul.testcontainers.model.dto.UserQueueDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserMessageQueueConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageQueueConsumer.class);


    private final UserService userService;

    public UserMessageQueueConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "${mq.user.update.queue-name}")
    public void consume(UserQueueDto userQueueDto) {
        try {
            logger.debug("Incoming user data for user change: {}", userQueueDto);
            User user = userService.updateUserEmail(userQueueDto);
            logger.debug("User updated with mail : {}", user.getEmail());
        } catch (RuntimeException ex) {
            logger.error("Error while posting for user : {}", userQueueDto);
            throw ex;
        }
    }

}
