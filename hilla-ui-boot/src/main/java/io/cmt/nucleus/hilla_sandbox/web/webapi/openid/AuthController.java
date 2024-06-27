package io.cmt.nucleus.hilla_sandbox.web.webapi.openid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cmt.nucleus.hilla_sandbox.ui.boot.cfg.KeycloakCfg;
import io.cmt.nucleus.hilla_sandbox.web.model.TokenBody;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/openid")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final KeycloakCfg kcCfg;

  public AuthController(KeycloakCfg kcCfg) {
    this.kcCfg = kcCfg;
  }

  @GetMapping("/auth")
  public ModelAndView auth(HttpServletRequest request, HttpServletResponse response,
                           ModelMap model) {
    model.addAttribute("redirect_uri", "http://localhost:8080/openid/callback");
    model.addAttribute("response_type", "code");
    model.addAttribute("client_id", kcCfg.clientId());
    model.addAttribute("scope", "openid");
    model.addAttribute("state", UUID.randomUUID().toString());

    final var referrer = request.getHeader("referer");
    logger.info("referrer: {}", referrer);
    response.addCookie(createCookie("cb_redirect_to", referrer, 600));

    return new ModelAndView("redirect:" + kcCfg.authEndpoint(), model);
  }

  @GetMapping("/callback")
  public ModelAndView callback(@RequestParam String code,
                               @RequestParam String state,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               ModelMap model) {
    logger.info("code: {}", code);
    final var tokens = tokens(code);
    updateCookies(response, tokens);
    model.addAttribute("code", code);
    model.addAttribute("state", state);
    model.addAttribute("access_token", tokens.access_token());

    final var redirectTo = cookieValue("cb_redirect_to", request).orElse("/");

    return new ModelAndView("redirect:" + redirectTo);
  }

  private void updateCookies(HttpServletResponse response, TokenBody tokens) {
    response.addCookie(
        createCookie("access_token", tokens.access_token(), tokens.expires_in()));
    response.addCookie(
        createCookie("refresh_token", tokens.refresh_token(), tokens.refresh_expires_in()));
    response.addCookie(createCookie("id_token", tokens.id_token(), tokens.refresh_expires_in()));

    final var now = LocalDateTime.now();
    final var accessTokenValidUntil = now.plusSeconds(tokens.expires_in());
    final var refreshTokenValidUntil = now.plusSeconds(tokens.refresh_expires_in());

    response.addCookie(createCookie("access_token_valid_until", accessTokenValidUntil.format(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME), tokens.expires_in()));
    response.addCookie(createCookie("refresh_token_valid_until", refreshTokenValidUntil.format(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME), tokens.refresh_expires_in()));
  }

  private TokenBody tokens(String code) {
    final var restTemplate = new RestTemplate();
    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    final var payload = new LinkedMultiValueMap<>();
    payload.add("client_id", kcCfg.clientId());
    payload.add("client_secret", kcCfg.clientSecret());
    payload.add("grant_type", "authorization_code");
    payload.add("redirect_uri", "http://localhost:8080/openid/callback");
    payload.add("code", code);

    final var request = new HttpEntity<>(payload, headers);

    final var tokenUrl = "http://localhost:8180/realms/nukleus/protocol/openid-connect/token";
    final var response = restTemplate.postForEntity(tokenUrl, request, String.class);
    logger.info("response: {}", response.getBody());

    final var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(response.getBody(), TokenBody.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private Cookie createCookie(String name, String value, int maxAge) {
    final var cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    return cookie;
  }

  private Optional<String> cookieValue(String name, HttpServletRequest request) {
    final var cookies = request.getCookies();
    if (cookies == null || cookies.length == 0) {
      return Optional.empty();
    }
    final var cookie = Arrays.stream(cookies)
        .filter(ck -> ck.getName().equals(name)).findFirst();
    return cookie.map(Cookie::getValue);
  }
}
