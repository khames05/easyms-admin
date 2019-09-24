package com.easyms.admin.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * @author khames
 */
public class BearerAuthHeaderProvider implements HttpHeadersProvider {

    private  final String META_DATA_ENABLE_OAUTH2 = "enable-oauth2";

    private final OAuth2RestTemplate template;

    public BearerAuthHeaderProvider(OAuth2RestTemplate template) {
        this.template = template;
    }

    public HttpHeaders getHeaders(Instance ignored) {

        HttpHeaders headers = new HttpHeaders();

        String oauth2Enabled = ignored.getRegistration().getMetadata().get(META_DATA_ENABLE_OAUTH2);
        if(oauth2Enabled != null && Boolean.valueOf(oauth2Enabled)) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + template.getAccessToken().getValue());
        }
        return headers;
    }
}