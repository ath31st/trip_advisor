package home.sweethome.tripadvisor.config;

import home.sweethome.tripadvisor.exceptionhandler.RestAccessDeniedHandler;
import home.sweethome.tripadvisor.exceptionhandler.RestAuthenticationEntryPoint;
import home.sweethome.tripadvisor.util.JWT.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static home.sweethome.tripadvisor.util.Role.ROLE_ADMINISTRATOR;
import static home.sweethome.tripadvisor.util.Role.ROLE_USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/login", "/api/auth/token","/api/user/register").permitAll()
                .antMatchers("/api/user/**").hasAuthority(ROLE_USER.name())
                .antMatchers("/api/trip/**").hasAuthority(ROLE_USER.name())
                .antMatchers("/api/user/delete/**").hasAuthority(ROLE_ADMINISTRATOR.name())
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable() //disable basic auth
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}