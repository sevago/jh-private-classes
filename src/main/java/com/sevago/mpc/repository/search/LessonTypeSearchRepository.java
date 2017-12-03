package com.sevago.mpc.repository.search;

import com.sevago.mpc.domain.LessonType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LessonType entity.
 */
public interface LessonTypeSearchRepository extends ElasticsearchRepository<LessonType, Long> {
}
