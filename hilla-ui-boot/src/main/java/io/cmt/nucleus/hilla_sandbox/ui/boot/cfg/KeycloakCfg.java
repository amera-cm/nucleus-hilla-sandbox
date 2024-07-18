package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

public record KeycloakCfg(String authEndpoint, String tokenEndpoint, String logoutEndpoint,
                          String clientId, String clientSecret) {
}
