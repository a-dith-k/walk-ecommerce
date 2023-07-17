package com.adith.walk.config;


import com.adith.walk.service.CustomerCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class CustomerSecurityConfiguration {

    @Bean
    UserDetailsService userDetailsService1(){

        return new CustomerCustomUserDetailsService();
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    @Bean
    public SecurityFilterChain securityFilterUser(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProviderCustomer());

        http
                .securityMatcher("/user/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**","/user/","**`/user/**")
                        .hasRole("USER")

                        .anyRequest().authenticated()
                )
                .formLogin(form->form.loginPage("/user/login").permitAll().defaultSuccessUrl("/").failureForwardUrl("/otp-login"))
                .logout((logout) -> logout.logoutUrl("/user/logout").permitAll().logoutSuccessUrl("/"));
        return http.build();
    }


    @Bean
    AuthenticationProvider authenticationProviderCustomer(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService1());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }

}
