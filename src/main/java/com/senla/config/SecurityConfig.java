package com.senla.config;

import com.senla.security.jwt.JwtAuthProvider;
import com.senla.security.jwt.JwtAuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.senla.model.entityenum.Role.ADMIN;
import static com.senla.model.entityenum.Role.MANAGER;
import static com.senla.model.entityenum.Role.ROOT;

@RequiredArgsConstructor
@EnableWebSecurity
@ComponentScan("com.senla")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthProvider jwtAuthProvider;
    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(jwtAuthProvider)
                .authorizeRequests()

                .antMatchers(HttpMethod.POST,"/v1/login", "/v1/users", "/v1/users/new").permitAll()
                .antMatchers(HttpMethod.POST,"/v1/logout").authenticated()

                .antMatchers(HttpMethod.GET,"/v1/rental-points/**").authenticated()
                .antMatchers(HttpMethod.POST, "/v1/rental-points/**").hasAnyAuthority(ADMIN.name(), ROOT.name())
                .antMatchers(HttpMethod.PATCH, "/v1/rental-points/**").hasAnyAuthority(ADMIN.name(), ROOT.name())
                .antMatchers(HttpMethod.DELETE, "/v1/rental-points/**").hasAnyAuthority(ADMIN.name(), ROOT.name())

                .antMatchers("/v1/scooters/**").hasAnyAuthority(MANAGER.name(), ADMIN.name(), ROOT.name())

                .antMatchers(HttpMethod.GET, "/v1/users/{id}").authenticated()
                .antMatchers(HttpMethod.PATCH, "/v1/users/{id}").authenticated()
                .antMatchers(HttpMethod.PATCH,"/v1/users/{id}/role").hasAnyAuthority(ADMIN.name(), ROOT.name())
                .antMatchers("/v1/users/**").hasAnyAuthority(MANAGER.name(), ADMIN.name(), ROOT.name())

                .and()
                .httpBasic();
    }
}

