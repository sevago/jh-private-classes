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
    private final ApplicationProperties.Login login = new ApplicationProperties.Login();

    public ApplicationProperties() {}

    public ApplicationProperties.Elasticsearch getElasticsearch() { return this.elasticsearch; }

    public ApplicationProperties.Login getLogin () { return this.login; }

    public static class Elasticsearch {

        private Boolean enabled = true;

        public Elasticsearch() {}

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Login {

        private int maxAttempts = 10;

        public Login() {}

        public int getMaxAttempts() { return this.maxAttempts; }

        public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    }
}
