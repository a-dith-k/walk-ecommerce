package com.adith.walk.config;


import com.adith.walk.service.AdminCustomUserDetailsService;
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
public class AdminSecurityConfiguration {


    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    UserDetailsService userDetailsServiceAdmin() {
        return new AdminCustomUserDetailsService();
    }

    @Bean
    AuthenticationProvider authenticationProviderAdmin() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceAdmin());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterAdmin(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProviderAdmin());

        http.
                securityMatcher("/admin/**")
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .formLogin((form) -> form
                        .loginPage("/admin/login").permitAll().
                        defaultSuccessUrl("/admin/dashboard")
                )
                .logout((logout) -> logout
                        .logoutUrl("/admin/logout").permitAll()
                        .logoutSuccessUrl("/admin/login?logout"));


        return http.build();
    }

}
