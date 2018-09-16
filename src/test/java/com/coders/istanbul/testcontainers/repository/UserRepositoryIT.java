package com.coders.istanbul.testcontainers.repository;

import com.coders.istanbul.testcontainers.builder.UserBuilder;
import com.coders.istanbul.testcontainers.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void it_should_find_user_by_email() {
        //given
        String email = "enes.acikoglu@gmail.com";

        User anUser = UserBuilder
                .anUser()
                .id(1L)
                .email(email)
                .name("Enes")
                .surName("Acikoglu")
                .phone("538290481")
                .build();

        testEntityManager.persistAndFlush(anUser);

        //when
        Optional<User> expectedUser = userRepository.findUserByEmail(email);

        //then
        User actual = expectedUser.get();
        assertThat(expectedUser).isPresent();
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("Enes");
        assertThat(actual.getSurName()).isEqualTo("Acikoglu");
        assertThat(actual.getEmail()).isEqualTo("enes.acikoglu@gmail.com");
        assertThat(actual.getPhone()).isEqualTo("538290481");
    }
}