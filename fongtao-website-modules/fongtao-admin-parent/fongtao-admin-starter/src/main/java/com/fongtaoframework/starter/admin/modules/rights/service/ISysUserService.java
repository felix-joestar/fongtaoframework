package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import java.util.Optional;

public interface ISysUserService {

    Optional<SysUser> findByUserCode(String userCode);

    Optional<SysUser> findByUserId(String userId);

    PageResult<SysUser> page(PageQuery pageQuery);

    boolean existsByUserCode(String sysUserCode, String excludedSysUserId);

    boolean save(SysUser user);

    boolean updateById(SysUser user);

    boolean deleteById(String sysUserId);
}
