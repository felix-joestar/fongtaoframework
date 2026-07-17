package com.fongtaoframework.starter.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;

public interface CrudMapper<E extends CrudEntity> extends BaseMapper<E> {
}
