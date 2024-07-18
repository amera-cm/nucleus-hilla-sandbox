package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

import com.google.common.cache.CacheBuilder;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Duration;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * IAM JWT/JWK Spring Configuration.
 */
@Configuration
@ParametersAreNonnullByDefault
public class JwtConfig {

  private final String jwkSetUrl;

  public JwtConfig(@Value("${kc.endpoints.jwk-set}") String jwkSetUrl) {
    this.jwkSetUrl = jwkSetUrl;
  }

  /**
   * JSON Web Key (JWK) source.
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    try {
      return new ImmutableJWKSet<>(JWKSet.load(new URL(jwkSetUrl)));
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * JWT Decoder Bean.
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    final var jwkSetCache =
        new ConcurrentMapCache(
            "jwkSetCache",
            CacheBuilder.newBuilder().expireAfterWrite(Duration.ofHours(12)).build().asMap(),
            false);
    return NimbusJwtDecoder.withJwkSetUri(jwkSetUrl).cache(jwkSetCache).build();
  }

  /**
   * JWT Encoder Bean.
   */
  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(jwkSource());
  }
}
