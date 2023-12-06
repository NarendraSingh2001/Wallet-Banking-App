package com.fullstack.WalletBankingApp.repositoryTest;

import com.fullstack.WalletBankingApp.Enum.Role;
import com.fullstack.WalletBankingApp.model.User;
import com.fullstack.WalletBankingApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        userRepository = mock(UserRepository.class);
    }

    @Test
    public void testSaveAndFindUser() {
        // Given
        String email = "pavan123@gmail.com";
        User user = new User();
        user.setFirstname("pavan");
        user.setLastname("kumar");
        user.setEmail(email);
        user.setPassword("pavan231#2");
        user.setRole(Role.USER);

        // When
        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Then
        Optional<User> foundUser = userRepository.findById(email);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstname()).isEqualTo("pavan");
        assertThat(foundUser.get().getEmail()).isEqualTo("pavan123@gmail.com");
        verify(userRepository, times(1)).findById(email);
    }
}
