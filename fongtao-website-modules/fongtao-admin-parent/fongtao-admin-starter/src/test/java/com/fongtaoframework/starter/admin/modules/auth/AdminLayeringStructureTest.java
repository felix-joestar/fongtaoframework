package com.fongtaoframework.starter.admin.modules.auth;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminLayeringStructureTest {

    private static final String ADMIN_ROOT_PACKAGE = "com.fongtaoframework.starter.admin";
    private static final String COMMON_PACKAGE = ADMIN_ROOT_PACKAGE + ".common";
    private static final String ROOT_PACKAGE = ADMIN_ROOT_PACKAGE + ".modules.auth";

    @Test
    void shouldExposeTheAdminStarterCommonPackageContract() {
        assertPackage("common.autoconfigure.AdminAutoConfiguration", COMMON_PACKAGE + ".autoconfigure");
        assertPackage("common.properties.AdminProperties", COMMON_PACKAGE + ".properties");
    }

    @Test
    void shouldExposeTheAdminAuthLayeringContract() {
        Class<?> controller = requiredClass("controller.AdminAuthController");
        Class<?> facade = requiredClass("facade.IAdminAuthFacade");
        Class<?> facadeImplementation = requiredClass("facade.impl.AdminAuthFacade");
        Class<?> service = requiredClass("service.ISysUserService");
        Class<?> serviceImplementation = requiredClass("service.impl.SysUserService");
        Class<?> mapper = requiredClass("mapper.SysUserMapper");

        assertEquals(ROOT_PACKAGE + ".controller", controller.getPackageName());
        assertTrue(facade.isInterface());
        assertTrue(facade.isAssignableFrom(facadeImplementation));
        assertTrue(service.isInterface());
        assertTrue(service.isAssignableFrom(serviceImplementation));
        assertTrue(hasConstructorDependency(controller, facade));
        assertFalse(hasConstructorDependency(facadeImplementation, mapper));
    }

    @Test
    void shouldKeepAdminAuthDomainAndDataTypesInTheModulePackage() {
        assertPackage("domain.entity.SysUser", ROOT_PACKAGE + ".domain.entity");
        assertPackage("domain.dto.LoginRequest", ROOT_PACKAGE + ".domain.dto");
        assertPackage("domain.dto.LoginResponse", ROOT_PACKAGE + ".domain.dto");
        assertPackage("domain.dto.LoginUserResponse", ROOT_PACKAGE + ".domain.dto");
        assertPackage("domain.dto.RefreshTokenRequest", ROOT_PACKAGE + ".domain.dto");
        assertPackage("mapper.SysUserMapper", ROOT_PACKAGE + ".mapper");
        assertPackage("converter.SysUserConverter", ROOT_PACKAGE + ".converter");
    }

    private void assertPackage(String typeName, String packageName) {
        assertEquals(packageName, requiredClass(typeName).getPackageName());
    }

    private boolean hasConstructorDependency(Class<?> type, Class<?> dependency) {
        return Arrays.stream(type.getDeclaredConstructors())
                .map(Constructor::getParameterTypes)
                .anyMatch(parameterTypes -> Arrays.asList(parameterTypes).contains(dependency));
    }

    private Class<?> requiredClass(String relativeTypeName) {
        String className = relativeTypeName.startsWith("common.")
                ? ADMIN_ROOT_PACKAGE + "." + relativeTypeName
                : ROOT_PACKAGE + "." + relativeTypeName;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError("缺少分层类型: " + className, ex);
        }
    }
}
