package io.cmt.nucleus.hilla_sandbox.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenBody(String id_token, String access_token, int expires_in, String refresh_token,
                        int refresh_expires_in) {
}
