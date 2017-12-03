package com.sevago.mpc.repository;

import com.sevago.mpc.domain.Instructor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Instructor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("select instructor from Instructor instructor where instructor.user.login = ?#{principal.username}")
    List<Instructor> findByUserIsCurrentUser();
    @Query("select distinct instructor from Instructor instructor left join fetch instructor.rates")
    List<Instructor> findAllWithEagerRelationships();

    @Query("select instructor from Instructor instructor left join fetch instructor.rates where instructor.id =:id")
    Instructor findOneWithEagerRelationships(@Param("id") Long id);

}
