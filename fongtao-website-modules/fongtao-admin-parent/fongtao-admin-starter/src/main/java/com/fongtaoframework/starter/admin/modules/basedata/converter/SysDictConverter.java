package com.fongtaoframework.starter.admin.modules.basedata.converter;

import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysDictConverter {

    SysDictRow toRow(SysDict entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysDict toEntity(SysDictCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysDict toEntity(SysDictUpdateParam param);
}
