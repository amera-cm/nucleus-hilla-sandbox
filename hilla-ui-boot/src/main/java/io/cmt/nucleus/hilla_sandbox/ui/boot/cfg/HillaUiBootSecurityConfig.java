package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

import static org.springframework.security.config.Customizer.withDefaults;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ParametersAreNonnullByDefault
public class HillaUiBootSecurityConfig {

  @Bean
  public static GrantedAuthorityDefaults noRolePrefixGrantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
  }

  @Bean
  @Order(1)
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            eh -> eh.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            cfg -> cfg.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
                .bearerTokenResolver(new NucleusBearerTokenResolver())
        );
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/openid/**", "/public/**", "/ui/public/**")
        .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .anyRequest()
                    .permitAll());
    return http.build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(VaadinWebSecurity.getDefaultHttpSecurityPermitMatcher())
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(formLogin ->
            formLogin
                .loginPage("/openid/auth")
        ).oauth2ResourceServer(
            cfg -> cfg.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
                .bearerTokenResolver(new NucleusBearerTokenResolver())
        );
    return http.build();
  }
}
