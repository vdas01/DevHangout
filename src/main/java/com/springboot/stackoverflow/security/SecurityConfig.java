package com.springboot.stackoverflow.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(config->
                        config
//                                .requestMatchers("/userProfile","/questions/ask",
//                                        "/savequestion", "/editQuestion{questionId}",
//                                        "/updateQuestion{questionId}", "/deleteQuestion{questionId}",
//                                        "/addAnswer{questionId}", "/editAnswer{answerId}","/updateAnswer{answerId}",
//                                        "/deleteAnswer{answerId}").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(form->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout->
                        logout
                                .permitAll());
        return http.build();
    }
}
