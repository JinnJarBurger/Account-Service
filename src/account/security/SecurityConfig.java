package account.security;

import account.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static account.model.Constants.*;

/**
 * @author adnan
 * @since 11/29/2022
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           RestAuthenticationEntryPoint authenticationEntryPoint,
                                           RestAccessDeniedHandler accessDeniedHandler) throws Exception {

        http
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/shutdown", "/h2-console", "/h2-console/**").permitAll()
                .mvcMatchers("/api/auth/signup").permitAll()
                .mvcMatchers("/api/auth/changepass").authenticated()
                .mvcMatchers("/api/empl/payment").hasAnyRole(USER, ACCOUNTANT)
                .mvcMatchers("/api/acct/payments").hasRole(ACCOUNTANT)
                .antMatchers("/api/admin/**").hasRole(ADMINISTRATOR)
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(getEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
