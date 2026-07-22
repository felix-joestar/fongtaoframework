package com.fongtaoframework.starter.file.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.dromara.x.file.storage.spring.EnableFileStorage;

@AutoConfiguration
@ConditionalOnClass(name = "org.dromara.x.file.storage.core.FileStorageService")
@EnableFileStorage
public class FileStorageAutoConfiguration {
}
