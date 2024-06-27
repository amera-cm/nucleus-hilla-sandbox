package io.cmt.nucleus.hilla_sandbox.ui.ops;

import org.springframework.stereotype.Component;

@Component
public class UiOps {

  public String sayHello(String name) {
    return "Hello from BEAN " + name;
  }
}
