package com.sevago.mpc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent>{

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String ipAddress = DomainUserDetailsService.getClientIP(this.request);
        String userLogin = (String) event.getAuthentication().getName();
        loginAttemptService.loginSucceeded(userLogin + "_" + ipAddress);
    }
}
