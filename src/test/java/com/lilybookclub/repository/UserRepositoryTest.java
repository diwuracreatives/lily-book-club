package com.lilybookclub.repository;

import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {
            User user = User.builder()
                    .email("ade@m.com")
                    .firstname("bola")
                    .lastname("aa")
                    .password("a10")
                    .role(Role.USER)
                    .build();

            User savedUser = userRepository.save(user);
            Assertions.assertNotNull(savedUser);
            Assertions.assertNotNull(savedUser.getId());
    }

    @Test
    public void UserRepository_FindAll_ReturnAllUsers() {
        User firstuser = User.builder()
                .email("ade@m.com")
                .firstname("bola")
                .lastname("ade")
                .password("adeb102")
                .role(Role.USER)
                .build();

        User secondUser = User.builder()
                .email("ige@m.com")
                .firstname("ige")
                .lastname("bola")
                .password("a10")
                .role(Role.USER)
                .build();

        userRepository.save(firstuser);
        userRepository.save(secondUser);

        List<User> users = userRepository.findAll();
        log.debug(users.toString());
        Assertions.assertNotNull(users);
    }


    @Test
    public void UserRepository_FindById_ReturnUser() {
        User firstuser = User.builder()
                .email("ade@m.com")
                .firstname("bola")
                .lastname("ade")
                .password("adeb102")
                .role(Role.USER)
                .build();

        userRepository.save(firstuser);

        User user = userRepository.findById(firstuser.getId()).get();
        Assertions.assertNotNull(user);
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserNotNull() {
        User user = User.builder()
                .email("ade@m.com")
                .firstname("bola")
                .lastname("ade")
                .password("adeb102")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail()).get();
        Assertions.assertNotNull(savedUser);

    }

    @Test
    public void UserRepository_Update_ReturnUpdatedUser() {
        User user = User.builder()
                .email("ade@m.com")
                .firstname("bola")
                .lastname("ade")
                .password("adeb102")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail()).get();

        savedUser.setEmail("igeadubi@k.com");
        savedUser.setFirstname("igeadubi");
        User savedUpdatedUser = userRepository.save(savedUser);
        Assertions.assertNotNull(savedUpdatedUser.getEmail());
        Assertions.assertNotNull(savedUpdatedUser.getFirstname());
    }

    @Test
    public void UserRepository_Delete_ReturnDeletedUser() {
        User user = User.builder()
                .email("ade@m.com")
                .firstname("bola")
                .lastname("ade")
                .password("adeb102")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> deletedUser = userRepository.findById(user.getId());
        Assertions.assertFalse(deletedUser.isPresent());
    }
}
