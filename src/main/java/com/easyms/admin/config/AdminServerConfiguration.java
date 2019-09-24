package com.easyms.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * @author khames
 */

@Configuration
public class AdminServerConfiguration extends AdminServerAutoConfiguration {

    @Value("${easyms.oauth2.client.clientId:}")
    private String clientId;
    @Value("${easyms.oauth2.client.clientSecret:}")
    private String clientSecret;
    @Value("${easyms.oauth2.client.userAuthorizationUri:}")
    private String accessTokenUri;

    public AdminServerConfiguration(AdminServerProperties adminServerProperties) {
        super(adminServerProperties);
    }

    @Bean
    public OAuth2ProtectedResourceDetails clientCredentialsResourceDetails() {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(accessTokenUri);
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        return details;
    }

    @Bean
    @Order(0)
    @ConditionalOnMissingBean
    public BearerAuthHeaderProvider bearerAuthHeaderProvider() {
        // couldn't inject differently restTemplate
        OAuth2ProtectedResourceDetails resourceDetails = this.clientCredentialsResourceDetails();
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
        return new BearerAuthHeaderProvider(oAuth2RestTemplate);
    }

}