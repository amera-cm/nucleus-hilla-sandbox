package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

import io.cmt.nucleus.hilla_sandbox.ui.ops.HelloReactUiOps;
import io.cmt.nucleus.hilla_sandbox.ui.ops.UiOps;
import io.cmt.nucleus.hilla_sandbox.web.webapi.RootWebApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {UiOps.class, HelloReactUiOps.class, RootWebApi.class})
public class HillaUiBootConfig {

  @Bean
  public KeycloakCfg keycloakCfg(
      @Value("${kc.endpoints.auth}") String authEndpoint,
      @Value("${kc.endpoints.token}") String tokenEndpoint,
      @Value("${kc.endpoints.logout}") String logoutEndpoint,
      @Value("${kc.client.id}") String clientId,
      @Value("${kc.client.secret}") String clientSecret
  ) {
    return new KeycloakCfg(authEndpoint, tokenEndpoint, logoutEndpoint, clientId, clientSecret);
  }
}
