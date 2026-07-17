package com.fongtaoframework.starter.mybatis.properties;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.mybatis")
public class MybatisStarterProperties {

    private DbType dbType = DbType.MYSQL;
    private boolean banner = false;
    private boolean paginationEnabled = true;
    private boolean optimisticLockerEnabled = true;
    private boolean blockAttackEnabled = true;
    private String logicDeleteField = "deleted";
    private String logicDeleteValue = "1";
    private String logicNotDeleteValue = "0";
    private IdType idType = IdType.ASSIGN_UUID;
    private FieldStrategy updateStrategy = FieldStrategy.ALWAYS;
}
