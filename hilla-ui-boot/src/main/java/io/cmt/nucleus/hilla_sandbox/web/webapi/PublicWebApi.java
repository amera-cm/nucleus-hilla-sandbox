package io.cmt.nucleus.hilla_sandbox.web.webapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicWebApi {
  @RequestMapping("/hello")
  public String hello() {
    return "hello public";
  }
}
