package com.fongtaoframework.admin.auth.facade;

import com.fongtaoframework.admin.auth.domain.dto.LoginRequest;
import com.fongtaoframework.admin.auth.domain.dto.LoginResponse;
import com.fongtaoframework.admin.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.admin.auth.domain.dto.RefreshTokenRequest;

public interface IAdminAuthFacade {

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    LoginUserResponse loginUser();

    void logout();
}
