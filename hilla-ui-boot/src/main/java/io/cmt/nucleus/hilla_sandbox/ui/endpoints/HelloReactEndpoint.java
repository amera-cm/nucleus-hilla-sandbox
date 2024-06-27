package io.cmt.nucleus.hilla_sandbox.ui.endpoints;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.hilla.Endpoint;
import io.cmt.nucleus.hilla_sandbox.ui.ops.UiOps;
import jakarta.annotation.security.RolesAllowed;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Endpoint
@Component
@RolesAllowed("SUPERUSER")
//@AnonymousAllowed
public class HelloReactEndpoint {

  private static final Logger logger = LogManager.getLogger(HelloReactEndpoint.class);

  private final UiOps uiOps;

  public HelloReactEndpoint(UiOps uiOps) {
    this.uiOps = uiOps;
  }

  @Nonnull
  public String sayHello(@Nonnull String name) {
    final var auth = SecurityContextHolder.getContext().getAuthentication();
    logger.info(auth.getAuthorities());
    final var authToken =
        (JwtAuthenticationToken) VaadinRequest.getCurrent().getUserPrincipal();
    final var jwt = authToken.getToken();
    logger.info(jwt);
    if (name.isEmpty()) {
      return "Hello stranger (%s)".formatted(jwt.getClaimAsString("email"));
    } else {
      return uiOps.sayHello("Hello %s (%s)".formatted(name, jwt.getClaimAsString("name")));
    }
  }
}
