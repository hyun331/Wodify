package com.soocompany.wodify.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    String applicationYml;

    private String corsUrl;


    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        if(applicationYml.equals("local")){
            corsUrl = "http://localhost:3000";
        }else{
            corsUrl = "https://www.wodify.site";
        }
        corsRegistry.addMapping("/**")
                .allowedOrigins(corsUrl)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
