package com.fongtaoframework.starter.mybatis.service.impl;

import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;
import com.fongtaoframework.starter.mybatis.service.ICrudEntityService;

public class CrudEntityService<M extends CrudMapper<E>, E extends CrudEntity>
        extends BaseEntityService<M, E> implements ICrudEntityService<E> {

    public static final String MSG_DATA_NOT_FOUND = "该数据不存在，请刷新后重试";
    public static final String MSG_INSERT_ERROR = "新增保存失败，请刷新后重试";
    public static final String MSG_UPDATE_ERROR = "更新保存失败，请刷新后重试";
    public static final String MSG_DELETE_ERROR = "删除失败，请刷新后重试";
    public static final String MSG_CODE_EXIST = "代码已存在，请修改后重试";
}
