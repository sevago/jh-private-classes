package com.sevago.mpc.repository;

import com.sevago.mpc.domain.Rate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Rate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select rate from Rate rate where rate.user.login = ?#{principal.username}")
    List<Rate> findByUserIsCurrentUser();

}
