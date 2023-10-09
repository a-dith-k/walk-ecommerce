package com.adith.walk.config;


import com.adith.walk.service.CustomerCustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CustomerSecurityConfiguration {

    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    UserDetailsService userDetailsServiceCustomer() {

        return new CustomerCustomUserDetailsService();
    }


    @Bean
    AuthenticationProvider authenticationProviderCustomer() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceCustomer());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;

    }


    @Bean
    public SecurityFilterChain securityFilterUser(HttpSecurity http) throws Exception {

        http
                .authenticationProvider(authenticationProviderCustomer());

        http.csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .securityMatcher("/user/**", "/", "/products/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/user/**")
                        .hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/user/login").permitAll()
                        .defaultSuccessUrl("/").permitAll()
                        .successHandler(new CustomerCustomAuthenticationSuccessHandler()).permitAll())

                .logout((logout) -> logout
                        .logoutUrl("/user/logout").permitAll()
                        .logoutSuccessUrl("/user/login?logout"));
        return http.build();
    }


}
