package com.sevago.mpc.security;

import com.sevago.mpc.config.ApplicationProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class LoginAttemptServiceTest {

    final private static String KEY = "key";

    LoginAttemptService loginAttemptService;

    @Mock
    ApplicationProperties applicationProperties;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginAttemptService = new LoginAttemptService(applicationProperties);
    }

    @Test
    public void assertThatLoginFailedCanIncrementAttemptsCounter() {
        //given
        String key = KEY;
        ApplicationProperties.Login login = new ApplicationProperties.Login();
        when(applicationProperties.getLogin()).thenReturn(login);

        //when
        IntStream.rangeClosed(1, login.getMaxAttempts())
            .forEach(i -> loginAttemptService.loginFailed(KEY));

        //then
        assertThat(loginAttemptService.isBlocked(KEY)).isTrue();
    }

    @Test
    public void assertThatLoginSucceededCanUnblockKey() {
        //given
        assertThatLoginFailedCanIncrementAttemptsCounter();

        //when
        loginAttemptService.loginSucceeded(KEY);

        //then
        assertThat(loginAttemptService.isBlocked(KEY)).isFalse();
    }
}
