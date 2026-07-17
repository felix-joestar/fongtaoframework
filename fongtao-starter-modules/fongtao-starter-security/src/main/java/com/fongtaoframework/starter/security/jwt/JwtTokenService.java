package com.fongtaoframework.starter.security.jwt;

import cn.hutool.core.collection.CollUtil;
import com.fongtaoframework.starter.security.properties.SecurityStarterProperties;
import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

public class JwtTokenService {

    private static final String TOKEN_TYPE_CLAIM = "typ";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String USER_ID_CLAIM = "user_id";
    private static final String USER_NAME_CLAIM = "name";
    private static final String AVATAR_FILE_ID_CLAIM = "avatar_file_id";
    private static final String AUTHORITIES_CLAIM = "authorities";

    private final SecurityStarterProperties properties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtTokenService(SecurityStarterProperties properties) {
        this.properties = properties;
        SecretKey secretKey = secretKey(properties.getJwt().getSecret());
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    public TokenValue createAccessToken(LoginUserDetails loginUser) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getJwt().getAccessTokenTtl());
        return createToken(loginUser, ACCESS_TOKEN_TYPE, now, expiresAt);
    }

    public TokenValue createRefreshToken(LoginUserDetails loginUser) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getJwt().getRefreshTokenTtl());
        return createToken(loginUser, REFRESH_TOKEN_TYPE, now, expiresAt);
    }

    public Authentication authenticate(String accessToken) {
        Jwt jwt = decode(accessToken);
        requireTokenType(jwt, ACCESS_TOKEN_TYPE);
        LoginUserDetails loginUser = toLoginUser(jwt);
        return new UsernamePasswordAuthenticationToken(loginUser, accessToken, loginUser.getAuthorities());
    }

    public RefreshTokenPrincipal parseRefreshToken(String refreshToken) {
        Jwt jwt = decode(refreshToken);
        requireTokenType(jwt, REFRESH_TOKEN_TYPE);
        String userId = jwt.getClaimAsString(USER_ID_CLAIM);
        String username = jwt.getSubject();
        if (userId == null || username == null) {
            throw new BadCredentialsException("refresh token 无效");
        }
        return new RefreshTokenPrincipal(userId, username);
    }

    private TokenValue createToken(LoginUserDetails loginUser, String tokenType, Instant issuedAt, Instant expiresAt) {
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer(properties.getJwt().getIssuer())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(loginUser.getUsername())
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .claim(USER_ID_CLAIM, loginUser.getUserId())
                .claim(AUTHORITIES_CLAIM, authorityNames(loginUser));
        claimIfNotNull(claimsBuilder, USER_NAME_CLAIM, loginUser.getName());
        claimIfNotNull(claimsBuilder, AVATAR_FILE_ID_CLAIM, loginUser.getAvatarFileId());
        JwtClaimsSet claims = claimsBuilder.build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String value = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenValue(value, expiresAt, Duration.between(issuedAt, expiresAt).toSeconds());
    }

    private void claimIfNotNull(JwtClaimsSet.Builder claimsBuilder, String claimName, Object value) {
        if (value != null) {
            claimsBuilder.claim(claimName, value);
        }
    }

    private Jwt decode(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException ex) {
            throw new BadCredentialsException("token 无效或已过期", ex);
        }
    }

    private void requireTokenType(Jwt jwt, String expectedType) {
        String actualType = jwt.getClaimAsString(TOKEN_TYPE_CLAIM);
        if (!Objects.equals(expectedType, actualType)) {
            throw new BadCredentialsException("token 类型无效");
        }
    }

    private LoginUserDetails toLoginUser(Jwt jwt) {
        List<GrantedAuthority> authorities = authorityList(jwt.getClaimAsStringList(AUTHORITIES_CLAIM));
        return new LoginUserDetails(
                jwt.getClaimAsString(USER_ID_CLAIM),
                jwt.getSubject(),
                jwt.getClaimAsString(USER_NAME_CLAIM),
                null,
                null,
                jwt.getClaimAsString(AVATAR_FILE_ID_CLAIM),
                authorities);
    }

    private List<String> authorityNames(LoginUserDetails loginUser) {
        return loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    private List<GrantedAuthority> authorityList(List<String> authorityNames) {
        if (CollUtil.isEmpty(authorityNames)) {
            return List.of();
        }
        return authorityNames.stream().map(SimpleGrantedAuthority::new).map(GrantedAuthority.class::cast).toList();
    }

    private SecretKey secretKey(String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("fongtao.security.jwt.secret 长度至少需要 32 字节");
        }
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }
}
