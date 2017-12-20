package com.sevago.mpc.service.impl;

import com.sevago.mpc.domain.MpcEntity;
import com.sevago.mpc.repository.UserRepository;
import com.sevago.mpc.security.AuthoritiesConstants;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class CommonServiceImpl {

    private final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public MpcEntity populateDefaultProperties(MpcEntity mpcEntity) {
        if (mpcEntity.getUser() == null && !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin().get());
            mpcEntity.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new InternalServerErrorException("Current user login not found"))
            ).get());
        }
        return mpcEntity;
    }

}
