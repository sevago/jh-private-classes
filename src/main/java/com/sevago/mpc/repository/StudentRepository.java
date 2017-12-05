package com.sevago.mpc.repository;

import com.sevago.mpc.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select student from Student student where student.user.login = ?#{principal.username}")
    List<Student> findByUserIsCurrentUser();

    Page<Student> findByUserLoginOrderByName(String currentUserLogin, Pageable pageable);

}
