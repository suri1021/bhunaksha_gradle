package in.nic.lrisd.bhunakshav6.bhunakshamain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST,"/mapinfo/*").permitAll();
                //.antMatchers(HttpMethod.POST, "/login").permitAll()
             //   .antMatchers(HttpMethod.POST,"/newuser/*").permitAll()
             //   .antMatchers(HttpMethod.GET,"/master/*").permitAll()
             //   .antMatchers(HttpMethod.GET,"/exploreCourse").permitAll()
             //   .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/WMS");
    }

}