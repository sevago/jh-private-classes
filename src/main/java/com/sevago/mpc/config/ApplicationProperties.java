package com.sevago.mpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Privateclasses.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final ApplicationProperties.Elasticsearch elasticsearch = new ApplicationProperties.Elasticsearch();

    public ApplicationProperties() {}

    public ApplicationProperties.Elasticsearch getElasticsearch() { return this.elasticsearch; }

    public static class Elasticsearch {

        private Boolean enabled = true;

        public Elasticsearch() {
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
