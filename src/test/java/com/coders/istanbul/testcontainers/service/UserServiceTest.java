package com.coders.istanbul.testcontainers.service;

import com.coders.istanbul.testcontainers.builder.UserBuilder;
import com.coders.istanbul.testcontainers.domain.User;
import com.coders.istanbul.testcontainers.model.dto.UserQueueDto;
import com.coders.istanbul.testcontainers.repository.UserRepository;
import com.coders.istanbul.testcontainers.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    public void it_should_update_user_email() {
        //given
        UserQueueDto userQueueDto = new UserQueueDto(1L, "enes.acikoglu@gmail.com");

        User existingUser = UserBuilder
                .anUser()
                .id(1L)
                .email("old.mail@gmail.com")
                .build();

        given(userRepository.findById(1L))
                .willReturn(Optional.of(existingUser));

        existingUser.setEmail("enes.acikoglu@gmail.com");

        given(userRepository.save(any(User.class)))
                .willReturn(existingUser);

        //when
        User updatedUser = userService.updateUserEmail(userQueueDto);

        //then
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getEmail()).isEqualTo("enes.acikoglu@gmail.com");
    }
}