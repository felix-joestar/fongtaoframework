package com.fongtaoframework.starter.admin.modules.rights.facade;

import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResUpdateParam;
import java.util.List;

public interface ISysResFacade {

    PageResult<SysResRow> page(SysResPageParam param);

    List<SysResRow> tree();

    SysResRow getById(String sysResId);

    void create(SysResCreateParam param);

    void updateById(SysResUpdateParam param);

    void deleteById(String sysResId);
}
