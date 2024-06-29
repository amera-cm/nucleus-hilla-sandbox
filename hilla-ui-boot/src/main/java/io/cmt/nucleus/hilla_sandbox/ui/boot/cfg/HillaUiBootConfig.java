package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

import io.cmt.nucleus.hilla_sandbox.ui.ops.HelloReactUiOps;
import io.cmt.nucleus.hilla_sandbox.ui.ops.UiOps;
import io.cmt.nucleus.hilla_sandbox.web.webapi.RootWebApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {UiOps.class, HelloReactUiOps.class, RootWebApi.class})
public class HillaUiBootConfig {

  @Bean
  public KeycloakCfg keycloakCfg() {
    return new KeycloakCfg("http://localhost:8180/realms/nukleus/protocol/openid-connect/auth",
        "camunda-pilot", "046Ki3znoKKKFMzQtDFrwycKM0kn32OY");
  }
}
