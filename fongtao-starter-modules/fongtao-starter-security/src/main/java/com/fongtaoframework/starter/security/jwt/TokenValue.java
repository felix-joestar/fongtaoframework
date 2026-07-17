package com.fongtaoframework.starter.security.jwt;

import java.time.Instant;

public record TokenValue(String token, Instant expiresAt, long expiresIn) {
}
