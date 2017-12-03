package com.sevago.mpc.repository.search;

import com.sevago.mpc.domain.Instructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Instructor entity.
 */
public interface InstructorSearchRepository extends ElasticsearchRepository<Instructor, Long> {
}
