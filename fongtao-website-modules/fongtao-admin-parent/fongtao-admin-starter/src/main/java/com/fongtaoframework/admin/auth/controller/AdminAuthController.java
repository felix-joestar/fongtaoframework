package com.fongtaoframework.admin.auth.controller;

import com.fongtaoframework.admin.auth.domain.dto.LoginRequest;
import com.fongtaoframework.admin.auth.domain.dto.LoginResponse;
import com.fongtaoframework.admin.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.admin.auth.domain.dto.RefreshTokenRequest;
import com.fongtaoframework.admin.auth.facade.IAdminAuthFacade;
import com.fongtaoframework.core.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AdminAuthController {

    private final IAdminAuthFacade adminAuthFacade;

    @PostMapping("/login")
    public ResponseEntity<R<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(R.success(adminAuthFacade.login(request)));
        } catch (DisabledException ex) {
            return failed(HttpStatus.FORBIDDEN, 403, ex.getMessage());
        } catch (BadCredentialsException ex) {
            return failed(HttpStatus.UNAUTHORIZED, 401, ex.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<R<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(R.success(adminAuthFacade.refreshToken(request)));
        } catch (DisabledException ex) {
            return failed(HttpStatus.FORBIDDEN, 403, ex.getMessage());
        } catch (BadCredentialsException ex) {
            return failed(HttpStatus.UNAUTHORIZED, 401, ex.getMessage());
        }
    }

    @GetMapping("/login-user")
    public ResponseEntity<R<LoginUserResponse>> loginUser() {
        try {
            return ResponseEntity.ok(R.success(adminAuthFacade.loginUser()));
        } catch (AuthenticationCredentialsNotFoundException ex) {
            return failed(HttpStatus.UNAUTHORIZED, 401, ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        adminAuthFacade.logout();
        return R.success();
    }

    private <T> ResponseEntity<R<T>> failed(HttpStatus status, int code, String message) {
        return ResponseEntity.status(status).body(R.failed(code, message));
    }
}
