package in.nic.lrisd.bhunakshav6.bhunakshamain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class SecurityConfiguration implements  WebMvcConfigurer{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/**").permitAll()
             //   .antMatchers(HttpMethod.POST,"/mapinfo/*").permitAll()
                .anyRequest().authenticated();
                //.antMatchers(HttpMethod.POST, "/login").permitAll()
             //   .antMatchers(HttpMethod.POST,"/newuser/*").permitAll()
             //   .antMatchers(HttpMethod.GET,"/master/*").permitAll()
             //   .antMatchers(HttpMethod.GET,"/exploreCourse").permitAll()
             //   .anyRequest().authenticated();
        return http.build();
    }
/*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
*/


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
       return (web) -> web.ignoring().antMatchers("/WMS");
    }

/*    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("http://localhost:3000");

        registry.addMapping("/mapinfo/*").allowedMethods("POST").allowedOrigins("*");
    }*/
}