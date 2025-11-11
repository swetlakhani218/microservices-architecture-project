package com.project.user.service.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        System.out.println("token: " + token);
        System.out.println("interceptor is working");
        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
