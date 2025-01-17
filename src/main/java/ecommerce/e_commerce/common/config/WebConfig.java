package ecommerce.e_commerce.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
     
    @Value("${app.api-prefix}")
    private String apiPrefix;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(@SuppressWarnings("null") PathMatchConfigurer configurer) {
                configurer.addPathPrefix(apiPrefix, c -> true);
            }
        };
    }

}
