package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ParametersAreNonnullByDefault
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";

  @SuppressWarnings("unchecked")
  @Nonnull
  private static List<SimpleGrantedAuthority> jwtToGrantedAuthorities(Jwt jwt) {
    final var authorities = new ArrayList<SimpleGrantedAuthority>();
    if (jwt.hasClaim(REALM_ACCESS_CLAIM)) {
      final var realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
      final var roles = (List<String>) realmAccess.get(ROLES_CLAIM);
      authorities.addAll(fromAuthNames(roles, null));
    }
    return authorities;
  }

  @Nonnull
  private static List<SimpleGrantedAuthority> fromAuthNames(
      List<String> authNames, @Nullable String prefix) {
    final var _prefix = Objects.requireNonNullElse(prefix, "");
    return authNames.stream()
        .map(name -> new SimpleGrantedAuthority(_prefix + name))
        .toList();
  }

  @Nonnull
  public AbstractAuthenticationToken convert(Jwt jwt) {
    return new JwtAuthenticationToken(jwt, jwtToGrantedAuthorities(jwt));
  }
}
