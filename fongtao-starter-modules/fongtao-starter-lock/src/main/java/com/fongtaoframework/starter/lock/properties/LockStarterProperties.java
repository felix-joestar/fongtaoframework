package com.fongtaoframework.starter.lock.properties;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.lock")
public class LockStarterProperties {

    private boolean enabled = true;

    private Duration defaultAcquireTimeout = Duration.ofSeconds(3);

    private Duration defaultExpire = Duration.ofSeconds(30);
}
