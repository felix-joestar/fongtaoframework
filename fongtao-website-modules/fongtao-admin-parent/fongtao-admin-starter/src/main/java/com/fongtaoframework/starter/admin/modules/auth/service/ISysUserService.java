package com.fongtaoframework.starter.admin.modules.auth.service;

import com.fongtaoframework.starter.admin.modules.auth.domain.entity.SysUser;
import java.util.Optional;

public interface ISysUserService {

    Optional<SysUser> findByUserCode(String userCode);

    Optional<SysUser> findByUserId(String userId);

    void assertEnabled(SysUser user);
}
