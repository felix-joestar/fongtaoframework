package com.fongtaoframework.admin.auth.service;

import com.fongtaoframework.admin.auth.domain.entity.SysUser;
import java.util.Optional;

public interface ISysUserService {

    Optional<SysUser> findByUserCode(String userCode);

    Optional<SysUser> findByUserId(String userId);

    void assertEnabled(SysUser user);
}
