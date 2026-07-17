package com.fongtaoframework.starter.mybatis.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fongtaoframework.starter.mybatis.domain.entity.BaseEntity;
import com.fongtaoframework.starter.mybatis.service.IBaseEntityService;

public class BaseEntityService<M extends BaseMapper<E>, E extends BaseEntity>
        extends ServiceImpl<M, E> implements IBaseEntityService<E> {
}
