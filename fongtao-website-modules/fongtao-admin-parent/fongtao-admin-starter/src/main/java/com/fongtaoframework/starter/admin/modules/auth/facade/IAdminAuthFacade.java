package com.fongtaoframework.starter.admin.modules.auth.facade;

import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginRequest;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.RefreshTokenRequest;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import java.util.List;

public interface IAdminAuthFacade {

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    LoginUserResponse loginUser();

    List<SysResRow> loginUserResources();

    void logout();
}
