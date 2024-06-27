package io.cmt.nucleus.hilla_sandbox.ui.boot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * HillaUiBootApp.
 */
@Theme(value = "hilla")
@SpringBootApplication
public class HillaUiBootApp implements AppShellConfigurator {

  public static void main(String[] args) {
    SpringApplication.run(HillaUiBootApp.class, args);
  }
}
