package home.sweethome.fussdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static home.sweethome.fussdb.util.Role.ROLE_ADMINISTRATOR;
import static home.sweethome.fussdb.util.Role.ROLE_USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//    private final JWTFilter jwtRequestFilter;
//    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
//
//    public SecurityConfig(JWTFilter jwtRequestFilter, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
//        this.jwtRequestFilter = jwtRequestFilter;
//        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/user/**").hasAuthority(ROLE_USER.name())
                .antMatchers("/api/trip/**").hasAuthority(ROLE_USER.name())
                .antMatchers("/api/user/delete").hasAuthority(ROLE_ADMINISTRATOR.name())
                .and()
             //   .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
             //   .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

       // httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}