package com.fongtaoframework.starter.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fongtaoframework.starter.mybatis.domain.entity.BaseEntity;

public interface IBaseEntityService<E extends BaseEntity> extends IService<E> {
}
