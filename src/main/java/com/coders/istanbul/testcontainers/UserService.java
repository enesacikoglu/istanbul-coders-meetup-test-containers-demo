package com.coders.istanbul.testcontainers;


import com.coders.istanbul.testcontainers.domain.User;
import com.coders.istanbul.testcontainers.model.dto.UserQueueDto;
import com.coders.istanbul.testcontainers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUserEmail(UserQueueDto userQueueDto) {
        User user = userRepository.findById(userQueueDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found!"));
        user.setEmail(userQueueDto.getEmail());
        return userRepository.save(user);
    }
}
