package com.fongtaoframework.starter.lock.autoconfigure;

import com.fongtaoframework.starter.lock.properties.LockStarterProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@ConditionalOnClass(name = "com.baomidou.lock.LockTemplate")
@ConditionalOnProperty(prefix = "fongtao.lock", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LockStarterProperties.class)
public class LockStarterAutoConfiguration {
}
