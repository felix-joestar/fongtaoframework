package com.fongtaoframework.starter.admin.modules.rights.facade;

import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysOrgRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgUpdateParam;
import java.util.List;

public interface ISysOrgFacade {

    PageResult<SysOrgRow> page(SysOrgPageParam param);

    List<SysOrgRow> tree();

    SysOrgRow getById(String sysOrgId);

    void create(SysOrgCreateParam param);

    void updateById(SysOrgUpdateParam param);

    void deleteById(String sysOrgId);
}
