package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import java.util.Optional;

public interface ISysUserService {

    Optional<SysUser> findByUserCode(String userCode);

    Optional<SysUser> findByUserId(String userId);

    PageResult<SysUser> page(PageQuery pageQuery);

    SysUser get(String userId);

    void save(SysUser user);

    void update(SysUser user);

    void delete(String userId);

    void updateStatus(String userId, Integer status);

    void resetPassword(String userId, String encodedPassword);

    void assertEnabled(SysUser user);
}
