package com.fongtaoframework.starter.admin.modules.rights.facade;

import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRightsRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateEnabledParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateParam;

public interface ISysRightsFacade {

    PageResult<SysRightsRow> page(SysRightsPageParam param);

    SysRightsRow getById(String sysRightsId);

    void create(SysRightsCreateParam param);

    void updateById(SysRightsUpdateParam param);

    void updateEnabled(SysRightsUpdateEnabledParam param);

    void deleteById(String sysRightsId);
}
