package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysSerialConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysSerialRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysSerialFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysSerialService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysSerialFacade implements ISysSerialFacade {

    private final ISysSerialService sysSerialService;
    private final SysSerialConverter sysSerialConverter;

    @Override
    public PageResult<SysSerialRow> page(SysSerialPageParam param) {
        SysSerialPageParam pageParam = param == null ? new SysSerialPageParam(null, null) : param;
        PageResult<SysSerial> page = sysSerialService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysSerialConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public SysSerialRow getById(String sysSerialId) {
        return sysSerialConverter.toRow(sysSerialService.get(sysSerialId));
    }

    @Override
    public void create(SysSerialCreateParam param) {
        SysSerial entity = sysSerialConverter.toEntity(param);
        entity.setSysSerialId(IdUtil.simpleUUID());
        sysSerialService.create(entity);
    }

    @Override
    public void updateById(SysSerialUpdateParam param) {
        sysSerialService.updateById(sysSerialConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysSerialId) {
        sysSerialService.deleteById(sysSerialId);
    }

    @Override
    public String next(String serialCode) {
        return sysSerialService.next(serialCode);
    }
}
