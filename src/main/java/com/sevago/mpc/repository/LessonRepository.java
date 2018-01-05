package com.sevago.mpc.repository;

import com.sevago.mpc.domain.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Lesson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("select distinct lesson from Lesson lesson left join fetch lesson.students")
    List<Lesson> findAllWithEagerRelationships();

    @Query("select lesson from Lesson lesson left join fetch lesson.students where lesson.id =:id")
    Lesson findOneWithEagerRelationships(@Param("id") Long id);

    Page<Lesson> findAllByOrderByDateDesc(Pageable pageable);

    Page<Lesson> findByTeachingInstructorUserLoginOrderByDateDesc(String currentUserLogin, Pageable pageable);

    List<Lesson> findAllByDateBetweenAndTeachingInstructorUserLogin(LocalDate firstDate, LocalDate secondDate, String login);

    List<Lesson> findAllByDateBetween(LocalDate firstDate, LocalDate secondDate);

}
