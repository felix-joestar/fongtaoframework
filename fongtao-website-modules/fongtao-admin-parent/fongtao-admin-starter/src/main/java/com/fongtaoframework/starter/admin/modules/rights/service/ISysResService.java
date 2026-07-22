package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import java.util.List;

public interface ISysResService {

    PageResult<SysRes> page(PageQuery pageQuery);

    List<SysRes> list();

    List<SysRes> listByIds(List<String> sysResIds);

    SysRes findById(String sysResId);

    boolean existsByCode(String sysResCode, String excludedSysResId);

    boolean existsByParentId(String parentId);

    boolean save(SysRes entity);

    boolean updateById(SysRes entity);

    boolean deleteById(String sysResId);
}
