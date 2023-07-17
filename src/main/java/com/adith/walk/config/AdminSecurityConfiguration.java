package com.adith.walk.config;


import com.adith.walk.service.AdminCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Order(1)
@Configuration
@EnableWebSecurity
public class AdminSecurityConfiguration {

    @Bean
    UserDetailsService userDetailsService(){
        return  new AdminCustomUserDetailsService();
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;




      @Bean
      public SecurityFilterChain securityFilterAdmin(HttpSecurity httpSecurity) throws Exception {
          httpSecurity.authenticationProvider(authenticationProviderAdmin());

           httpSecurity.
                   securityMatcher("/admin/**")
                    .csrf(c->c.disable())
                    .authorizeHttpRequests(auth->auth
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/login"))
                            .permitAll()
                            .requestMatchers("/admin/**")
                            .hasRole("ADMIN")
                    .anyRequest().authenticated())

                    .formLogin((form) -> form
                            .loginPage("/admin/login").defaultSuccessUrl("/admin/dashboard")
                    )
                    .logout((logout) -> logout.logoutUrl("/admin/logout").permitAll().logoutSuccessUrl("/admin/login"));


            return httpSecurity.build();
      }






    @Bean
    AuthenticationProvider authenticationProviderAdmin(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }











}
