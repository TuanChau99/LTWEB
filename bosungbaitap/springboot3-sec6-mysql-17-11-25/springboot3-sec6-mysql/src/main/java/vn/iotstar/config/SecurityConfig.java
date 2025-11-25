package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import vn.iotstar.Springboot3Sec6MysqlApplication;





@Configuration

@EnableWebSecurity

public class SecurityConfig {

@Autowired
    private final UserDetailsService userDetailsService;

    SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


UserInfoRepository repository;



// authentication

@Bean

UserDetailsService userDetailsService() {


return new UserInfoService(repository);

}



@Bean

PasswordEncoder passwordEncoder() {

return new BCryptPasswordEncoder();

}

@Bean

AuthenticationProvider authenticationProvider(){

DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();

authenticationProvider.setUserDetailsService(userDetailsService());

authenticationProvider.setPasswordEncoder(passwordEncoder());

return authenticationProvider;

}



//security 6.1+

@Bean

SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

return http.csrf(csrf -> csrf.disable())

.authorizeHttpRequests(auth -> auth

.requestMatchers("/user/new").permitAll()

.requestMatchers("/").permitAll()

.requestMatchers("/customer/**").authenticated()

//.anyRequest().authenticated()

)

.formLogin(Customizer.withDefaults())

.build();


}


}