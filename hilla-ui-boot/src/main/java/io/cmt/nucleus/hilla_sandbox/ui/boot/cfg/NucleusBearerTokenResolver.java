package io.cmt.nucleus.hilla_sandbox.ui.boot.cfg;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

public final class NucleusBearerTokenResolver implements BearerTokenResolver {

  private static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";
  private static final String ACCESS_TOKEN_COOKIE_NAME = ACCESS_TOKEN_PARAMETER_NAME;

  private static final Pattern authorizationPattern =
      Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
          Pattern.CASE_INSENSITIVE);

  private boolean allowFormEncodedBodyParameter = true;

  private boolean allowUriQueryParameter = true;

  private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

  private static Optional<String> resolveFromRequestParameters(HttpServletRequest request) {
    String[] values = request.getParameterValues(ACCESS_TOKEN_PARAMETER_NAME);
    if (values == null || values.length == 0) {
      return Optional.empty();
    }
    if (values.length == 1) {
      return Optional.of(values[0]);
    }
    BearerTokenError error =
        BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
    throw new OAuth2AuthenticationException(error);
  }

  private static boolean isGetRequest(HttpServletRequest request) {
    return HttpMethod.GET.name().equals(request.getMethod());
  }

  private static boolean isFormEncodedRequest(HttpServletRequest request) {
    return MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType());
  }

  private static boolean hasAccessTokenInQueryString(HttpServletRequest request) {
    return (request.getQueryString() != null) &&
        request.getQueryString().contains(ACCESS_TOKEN_PARAMETER_NAME);
  }

  private static Optional<String> resolveFromCookie(HttpServletRequest request) {
    final var cookies = request.getCookies();
    if (cookies == null || cookies.length == 0) {
      return Optional.empty();
    }
    final var cookie = Arrays.stream(cookies)
        .filter(ck -> ck.getName().equals(ACCESS_TOKEN_COOKIE_NAME)).findFirst();
    return cookie.map(Cookie::getValue);
  }

  @Override
  public String resolve(final HttpServletRequest request) {
    final var authorizationHeaderToken = resolveFromAuthorizationHeader(request);
    final var parameterToken = isParameterTokenSupportedForRequest(request)
        ? resolveFromRequestParameters(request) : Optional.<String>empty();
    final var cookieToken = resolveFromCookie(request);

    final var candidates = new ArrayList<String>();
    authorizationHeaderToken.ifPresent(candidates::add);
    parameterToken.ifPresent(candidates::add);
    cookieToken.ifPresent(candidates::add);

    if (candidates.size() == 1) {
      return candidates.getFirst();
    } else if (candidates.size() > 1) {
      final var error = BearerTokenErrors.invalidRequest(
          "Found multiple bearer tokens in the request. Check the request headers, query string, or cookies");
      throw new OAuth2AuthenticationException(error);
    } else {
      return null;
    }
  }

  /**
   * Set if transport of access token using form-encoded body parameter is supported. Defaults to
   * {@code true}.
   *
   * @param allowFormEncodedBodyParameter if the form-encoded body parameter is supported
   */
  public void setAllowFormEncodedBodyParameter(boolean allowFormEncodedBodyParameter) {
    this.allowFormEncodedBodyParameter = allowFormEncodedBodyParameter;
  }

  /**
   * Set if transport of access token using URI query parameter is supported. Defaults to
   * {@code true}.
   *
   * The spec recommends against using this mechanism for sending bearer tokens, and even goes as
   * far as stating that it was only included for completeness.
   *
   * @param allowUriQueryParameter if the URI query parameter is supported
   */
  public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
    this.allowUriQueryParameter = allowUriQueryParameter;
  }

  /**
   * Set this value to configure what header is checked when resolving a Bearer Token. This value is
   * defaulted to {@link HttpHeaders#AUTHORIZATION}.
   *
   * This allows other headers to be used as the Bearer Token source such as
   * {@link HttpHeaders#PROXY_AUTHORIZATION}
   *
   * @param bearerTokenHeaderName the header to check when retrieving the Bearer Token.
   * @since 5.4
   */
  public void setBearerTokenHeaderName(String bearerTokenHeaderName) {
    this.bearerTokenHeaderName = bearerTokenHeaderName;
  }

  private Optional<String> resolveFromAuthorizationHeader(HttpServletRequest request) {
    String authorization = request.getHeader(this.bearerTokenHeaderName);
    if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
      return Optional.empty();
    }
    Matcher matcher = authorizationPattern.matcher(authorization);
    if (!matcher.matches()) {
      BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
      throw new OAuth2AuthenticationException(error);
    }
    return Optional.ofNullable(matcher.group("token"));
  }

  private boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
    return isFormEncodedRequest(request) || isGetRequest(request);
  }

  private boolean isParameterTokenEnabledForRequest(HttpServletRequest request) {
    return ((this.allowFormEncodedBodyParameter && isFormEncodedRequest(request) &&
        !isGetRequest(request)
        && !hasAccessTokenInQueryString(request)) ||
        (this.allowUriQueryParameter && isGetRequest(request)));
  }

}
