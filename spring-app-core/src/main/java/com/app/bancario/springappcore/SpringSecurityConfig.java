package com.app.bancario.springappcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import com.app.bancario.springappcore.service.JpaUsuariosService;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private JpaUsuariosService usuariosService;
    
    @Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers( "/" , "/css/**" , "/assets/**" , "/img/**" , "/home" , "/index" , "/nosotros" , "/register/**" , "/verify/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/usuario/login")
            .permitAll()
            .and()
            .logout().permitAll();

        return http.build();
    }

    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(usuariosService);
		
	}
}
