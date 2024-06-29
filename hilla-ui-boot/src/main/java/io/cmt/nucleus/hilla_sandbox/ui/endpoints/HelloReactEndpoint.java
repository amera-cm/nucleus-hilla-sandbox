package io.cmt.nucleus.hilla_sandbox.ui.endpoints;

import com.vaadin.hilla.Endpoint;
import javax.annotation.Nonnull;

@Endpoint
public interface HelloReactEndpoint {

  String NAME = "HelloReactEndpoint";

  @Nonnull
  String sayHello(@Nonnull String name);
}
