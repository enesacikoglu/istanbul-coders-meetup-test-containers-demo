package com.coders.istanbul.testcontainers.repository;

import com.coders.istanbul.testcontainers.builder.UserBuilder;
import com.coders.istanbul.testcontainers.domain.User;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(initializers = UserRepositoryTestContainersIT.Initializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTestContainersIT {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @ClassRule
    public static PostgreSQLContainer sqlContainer = new PostgreSQLContainer();

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

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.hikari.username=" + sqlContainer.getUsername(),
                    "spring.datasource.hikari.password=" + sqlContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}