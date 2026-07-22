package com.fongtaoframework.starter.admin.modules.rights.facade;

import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRoleRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRolePageParam;
import java.util.List;

public interface ISysRoleFacade {

    PageResult<SysRoleRow> page(SysRolePageParam param);

    List<SysRoleRow> tree();

    SysRoleRow getById(String sysRoleId);

    void create(SysRoleCreateParam param);

    void updateById(SysRoleUpdateParam param);

    void deleteById(String sysRoleId);
}
