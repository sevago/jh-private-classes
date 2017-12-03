package com.sevago.mpc.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Lesson.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Lesson.class.getName() + ".students", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Lesson.class.getName() + ".invoices", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Instructor.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Instructor.class.getName() + ".invoices", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Instructor.class.getName() + ".lessons", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Instructor.class.getName() + ".rates", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.LessonType.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Student.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Student.class.getName() + ".invoices", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Student.class.getName() + ".lessons", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Invoice.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Invoice.class.getName() + ".lessons", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Rate.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Rate.class.getName() + ".instructors", jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Activity.class.getName(), jcacheConfiguration);
            cm.createCache(com.sevago.mpc.domain.Location.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
