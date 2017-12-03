package com.sevago.mpc.repository;

import com.sevago.mpc.domain.Location;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select location from Location location where location.user.login = ?#{principal.username}")
    List<Location> findByUserIsCurrentUser();

}
