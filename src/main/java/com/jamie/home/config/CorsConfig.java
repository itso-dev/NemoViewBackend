package com.jamie.home.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOriginPattern("*");
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");

      source.registerCorsConfiguration("/member/**", config);
      source.registerCorsConfiguration("/faq/**", config);
      source.registerCorsConfiguration("/review/**", config);
      source.registerCorsConfiguration("/report/**", config);
      source.registerCorsConfiguration("/contact/**", config);
      source.registerCorsConfiguration("/question/**", config);
      source.registerCorsConfiguration("/info/**", config);
      source.registerCorsConfiguration("/point/**", config);
      source.registerCorsConfiguration("/admin/**", config);
      source.registerCorsConfiguration("/main/**", config);
      return new CorsFilter(source);
   }

}
