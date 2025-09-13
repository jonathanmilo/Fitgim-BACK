package com.fit.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse (
       @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("user_Id")
        String user_Id,
        @JsonProperty("username")
        String username,
        @JsonProperty("email")
        String email
){}        

