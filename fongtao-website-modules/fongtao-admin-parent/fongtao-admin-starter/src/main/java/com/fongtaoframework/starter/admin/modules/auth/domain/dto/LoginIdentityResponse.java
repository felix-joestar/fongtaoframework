package com.fongtaoframework.starter.admin.modules.auth.domain.dto;

import java.util.List;

public record LoginIdentityResponse(
        String rightsId,
        String orgId,
        String roleId,
        String dataScope,
        List<String> orgIds) {}
