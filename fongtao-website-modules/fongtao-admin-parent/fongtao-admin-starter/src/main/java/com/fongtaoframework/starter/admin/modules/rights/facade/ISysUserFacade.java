package com.fongtaoframework.starter.admin.modules.rights.facade;

import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysUserRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdatePasswordParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateStatusParam;

public interface ISysUserFacade {

    PageResult<SysUserRow> page(SysUserPageParam param);

    SysUserRow getById(String sysUserId);

    void create(SysUserCreateParam param);

    void updateById(SysUserUpdateParam param);

    void deleteById(String sysUserId);

    void updateStatus(SysUserUpdateStatusParam param);

    void updatePassword(SysUserUpdatePasswordParam param);
}
