package com.fongtaoframework.starter.admin.modules.basedata.converter;

import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysDictItemConverter {

    SysDictItemRow toRow(SysDictItem entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysDictItem toEntity(SysDictItemCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysDictItem toEntity(SysDictItemUpdateParam param);
}
