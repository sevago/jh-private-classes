package com.sevago.mpc.repository;

import com.sevago.mpc.domain.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Preferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    @Query("select preferences from Preferences preferences where preferences.user.login = ?#{principal.username}")
    List<Preferences> findByUserIsCurrentUser();
}
