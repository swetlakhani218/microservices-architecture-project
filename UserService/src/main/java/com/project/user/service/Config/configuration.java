package com.project.user.service.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class configuration {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        // Add interceptor to RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Add interceptor to attach JWT token automatically
        ClientHttpRequestInterceptor jwtInterceptor = (request, body, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() != null) {
                String token = authentication.getCredentials().toString();
                request.getHeaders().set("Authorization", "Bearer " + token);
                System.out.println("🪪 JWT token added to internal request headers");
            } else {
                System.out.println("⚠️ No authentication token found in SecurityContext");
            }

            return execution.execute(request, body);
        };

        restTemplate.getInterceptors().add(jwtInterceptor);

        return restTemplate;
    }
}
