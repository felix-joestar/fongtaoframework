package com.fongtaoframework.starter.admin.common.autoconfigure;

import javax.sql.DataSource;

import com.fongtaoframework.starter.admin.modules.auth.facade.IAdminAuthFacade;
import com.fongtaoframework.starter.admin.modules.basedata.controller.SysConfigController;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysConfigMapper;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictItemMapper;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictMapper;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysSerialMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysOrgMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysResMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsExtraMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleAuthMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysUserMapper;
import com.fongtaoframework.starter.admin.modules.rights.controller.SysUserController;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.redisson.api.RedissonClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AdminAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AdminAutoConfiguration.class))
            .withPropertyValues("fongtao.security.enabled=false")
            .withBean(DataSource.class, () -> mock(DataSource.class))
            .withBean(SqlSessionFactory.class, AdminAutoConfigurationTest::sqlSessionFactory)
            .withBean(CacheManager.class, () -> mock(CacheManager.class))
            .withBean(PasswordEncoder.class, () -> mock(PasswordEncoder.class))
            .withBean(JwtTokenService.class, () -> mock(JwtTokenService.class))
            .withBean(RedissonClient.class, () -> mock(RedissonClient.class));

    @Test
    void shouldScanAdminModuleComponentsAndAnnotatedMappers() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(IAdminAuthFacade.class);
            assertThat(context).hasSingleBean(ISysUserService.class);
            assertThat(context).hasSingleBean(ISysDictService.class);
            assertThat(context).hasSingleBean(SysUserController.class);
            assertThat(context).hasSingleBean(SysConfigController.class);
            for (Class<?> mapperType : new Class<?>[] {
                    SysConfigMapper.class,
                    SysDictItemMapper.class,
                    SysDictMapper.class,
                    SysSerialMapper.class,
                    SysOrgMapper.class,
                    SysResMapper.class,
                    SysRightsExtraMapper.class,
                    SysRightsMapper.class,
                    SysRoleAuthMapper.class,
                    SysRoleMapper.class,
                    SysUserMapper.class
            }) {
                assertThat(context).hasSingleBean(mapperType);
            }
        });
    }

    @Test
    void shouldNotRegisterAdminModulesWhenDisabled() {
        contextRunner.withPropertyValues("fongtao.admin.enabled=false").run(context -> {
            assertThat(context).doesNotHaveBean(IAdminAuthFacade.class);
            assertThat(context).doesNotHaveBean(SysUserMapper.class);
            assertThat(context).doesNotHaveBean(SysUserController.class);
        });
    }

    private static SqlSessionFactory sqlSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("test", new JdbcTransactionFactory(), mock(DataSource.class)));
        return new DefaultSqlSessionFactory(configuration);
    }
}
