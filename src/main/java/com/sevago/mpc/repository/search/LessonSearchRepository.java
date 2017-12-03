package com.sevago.mpc.repository.search;

import com.sevago.mpc.domain.Lesson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Lesson entity.
 */
public interface LessonSearchRepository extends ElasticsearchRepository<Lesson, Long> {
}
