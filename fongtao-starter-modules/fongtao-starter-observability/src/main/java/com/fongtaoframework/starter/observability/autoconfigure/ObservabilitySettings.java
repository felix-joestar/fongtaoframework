package com.fongtaoframework.starter.observability.autoconfigure;

import java.util.List;

public record ObservabilitySettings(boolean arthasEnabled, List<String> endpointExposureInclude) {
}
