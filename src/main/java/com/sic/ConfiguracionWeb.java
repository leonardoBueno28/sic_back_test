package com.sic;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sic.core.manejoRepositorio.impl.RepositorioBaseImpl;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.sic.repositorios", repositoryBaseClass = RepositorioBaseImpl.class)
public class ConfiguracionWeb implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedOrigins("http://localhost:4200", "*");
    }

}
