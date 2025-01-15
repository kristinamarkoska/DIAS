package mk.tradesense.tradesense.config;

import mk.tradesense.tradesense.jason_web.JwtAuthEntryPoint;
import mk.tradesense.tradesense.jason_web.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    public ApplicationSecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public JwtRequestFilter jwtAuthenticationFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.authorizeHttpRequests(authRequests ->
                authRequests.requestMatchers("/auth/login", "/auth/register").permitAll()
                        .anyRequest().authenticated());

        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(handler ->
                handler.authenticationEntryPoint(jwtAuthEntryPoint));

        httpSecurity.headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin()));

        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
