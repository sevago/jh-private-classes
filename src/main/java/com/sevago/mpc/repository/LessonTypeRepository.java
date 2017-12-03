package com.sevago.mpc.repository;

import com.sevago.mpc.domain.LessonType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the LessonType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonTypeRepository extends JpaRepository<LessonType, Long> {

    @Query("select lesson_type from LessonType lesson_type where lesson_type.user.login = ?#{principal.username}")
    List<LessonType> findByUserIsCurrentUser();

}
