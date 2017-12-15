package com.sevago.mpc.security;

import com.sevago.mpc.PrivateclassesApp;
import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.domain.User;
import com.sevago.mpc.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrivateclassesApp.class)
@Transactional
public class DomainUserDetailsServiceIntTest {

    final public static String USER = "test";
    final public static String EMAIL = "test@localhost";

    final public static String RIGHT_PASSWORD = "right_password";
    final public static String WRONG_PASSWORD = "wrong_password";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    private HttpServletRequest request;

    @SpyBean
    private LoginAttemptService loginAttemptService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private int maxAttempts;

    @Before
    public void setUp() throws Exception {
        User user = new User();
        user.setLogin(USER);
        user.setEmail(EMAIL);
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(RIGHT_PASSWORD));
        userRepository.saveAndFlush(user);

        maxAttempts = applicationProperties.getLogin().getMaxAttempts();
    }

    @After
    public void teatDown() throws Exception {
        userRepository.deleteAll();
    }

    private void attemptAuthentication(Authentication authenticationToken) {
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {}
    }

    @Test
    public void loadUserByUsernameShouldThrowBlockedException() {
        //given
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(USER, WRONG_PASSWORD);

        //when
        IntStream.rangeClosed(1, maxAttempts)
            .forEach(i -> attemptAuthentication(authenticationToken));

        //then
        verify(loginAttemptService, times(maxAttempts)).isBlocked(anyString());

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("You have been blocked due to "+ maxAttempts + " repeated failed sign in attempts");

        authenticationManager.authenticate(authenticationToken);
    }
}
