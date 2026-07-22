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
    private static final String AUTH_ROOT_PACKAGE = ADMIN_ROOT_PACKAGE + ".modules.auth";
    private static final String RIGHTS_ROOT_PACKAGE = ADMIN_ROOT_PACKAGE + ".modules.rights";

    @Test
    void shouldExposeTheAdminStarterCommonPackageContract() {
        assertAdminPackage("common.autoconfigure.AdminAutoConfiguration", COMMON_PACKAGE + ".autoconfigure");
        assertAdminPackage("common.properties.AdminProperties", COMMON_PACKAGE + ".properties");
    }

    @Test
    void shouldExposeTheAdminAuthLayeringContract() {
        Class<?> controller = requiredClass("controller.AdminAuthController");
        Class<?> facade = requiredClass("facade.IAdminAuthFacade");
        Class<?> facadeImplementation = requiredClass("facade.impl.AdminAuthFacade");
        Class<?> service = requiredRightsClass("service.ISysUserService");
        Class<?> serviceImplementation = requiredRightsClass("service.impl.SysUserService");
        Class<?> mapper = requiredRightsClass("mapper.SysUserMapper");

        assertEquals(AUTH_ROOT_PACKAGE + ".controller", controller.getPackageName());
        assertTrue(facade.isInterface());
        assertTrue(facade.isAssignableFrom(facadeImplementation));
        assertTrue(service.isInterface());
        assertTrue(service.isAssignableFrom(serviceImplementation));
        assertTrue(hasConstructorDependency(controller, facade));
        assertTrue(hasConstructorDependency(facadeImplementation, service));
        assertFalse(hasConstructorDependency(facadeImplementation, mapper));
    }

    @Test
    void shouldKeepUserDataInRightsAndAuthenticationContractsInAuth() {
        assertRightsPackage("domain.entity.SysUser", RIGHTS_ROOT_PACKAGE + ".domain.entity");
        assertAuthPackage("domain.dto.LoginRequest", AUTH_ROOT_PACKAGE + ".domain.dto");
        assertAuthPackage("domain.dto.LoginResponse", AUTH_ROOT_PACKAGE + ".domain.dto");
        assertAuthPackage("domain.dto.LoginUserResponse", AUTH_ROOT_PACKAGE + ".domain.dto");
        assertAuthPackage("domain.dto.RefreshTokenRequest", AUTH_ROOT_PACKAGE + ".domain.dto");
        assertRightsPackage("mapper.SysUserMapper", RIGHTS_ROOT_PACKAGE + ".mapper");
        assertAuthPackage("converter.LoginUserConverter", AUTH_ROOT_PACKAGE + ".converter");
        assertRightsPackage("converter.SysUserConverter", RIGHTS_ROOT_PACKAGE + ".converter");
    }


    @Test
    void shouldExposeIndependentTableObjectLayers() {
        assertTableObjectLayer(RIGHTS_ROOT_PACKAGE, "SysUser");
        assertTableObjectLayer(RIGHTS_ROOT_PACKAGE, "SysOrg");
        assertTableObjectLayer(RIGHTS_ROOT_PACKAGE, "SysRole");
        assertTableObjectLayer(RIGHTS_ROOT_PACKAGE, "SysRes");
        assertTableObjectLayer(RIGHTS_ROOT_PACKAGE, "SysRoleAuth");
        assertTableObjectLayer(RIGHTS_ROOT_PACKAGE, "SysRights");
        String basedataRootPackage = ADMIN_ROOT_PACKAGE + ".modules.basedata";
        assertTableObjectLayer(basedataRootPackage, "SysDict");
        assertTableObjectLayer(basedataRootPackage, "SysDictItem");
        assertTableObjectLayer(basedataRootPackage, "SysConfig");
        assertTableObjectLayer(basedataRootPackage, "SysSerial");
    }

    private void assertTableObjectLayer(String moduleRootPackage, String objectName) {
        Class<?> controller = requiredModuleClass(moduleRootPackage, "controller." + objectName + "Controller");
        Class<?> facade = requiredModuleClass(moduleRootPackage, "facade.I" + objectName + "Facade");
        Class<?> facadeImplementation = requiredModuleClass(moduleRootPackage, "facade.impl." + objectName + "Facade");
        Class<?> service = requiredModuleClass(moduleRootPackage, "service.I" + objectName + "Service");
        Class<?> serviceImplementation = requiredModuleClass(moduleRootPackage, "service.impl." + objectName + "Service");
        Class<?> mapper = requiredModuleClass(moduleRootPackage, "mapper." + objectName + "Mapper");
        Class<?> converter = requiredModuleClass(moduleRootPackage, "converter." + objectName + "Converter");

        assertTrue(facade.isInterface());
        assertTrue(service.isInterface());
        assertTrue(facade.isAssignableFrom(facadeImplementation));
        assertTrue(service.isAssignableFrom(serviceImplementation));
        assertTrue(hasConstructorDependency(controller, facade));
        assertTrue(hasConstructorDependency(facadeImplementation, service));
        assertFalse(hasConstructorDependency(facadeImplementation, mapper));
        assertEquals(moduleRootPackage + ".converter", converter.getPackageName());
    }

    private Class<?> requiredModuleClass(String moduleRootPackage, String relativeTypeName) {
        String className = moduleRootPackage + "." + relativeTypeName;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError("missing layered type: " + className, ex);
        }
    }

    private void assertAuthPackage(String typeName, String packageName) {
        assertEquals(packageName, requiredClass(typeName).getPackageName());
    }

    private void assertRightsPackage(String typeName, String packageName) {
        assertEquals(packageName, requiredRightsClass(typeName).getPackageName());
    }

    private void assertAdminPackage(String typeName, String packageName) {
        assertEquals(packageName, requiredAdminClass(typeName).getPackageName());
    }

    private boolean hasConstructorDependency(Class<?> type, Class<?> dependency) {
        return Arrays.stream(type.getDeclaredConstructors())
                .map(Constructor::getParameterTypes)
                .anyMatch(parameterTypes -> Arrays.asList(parameterTypes).contains(dependency));
    }

    private Class<?> requiredClass(String relativeTypeName) {
        String className = AUTH_ROOT_PACKAGE + "." + relativeTypeName;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError("缺少分层类型: " + className, ex);
        }
    }

    private Class<?> requiredRightsClass(String relativeTypeName) {
        String className = RIGHTS_ROOT_PACKAGE + "." + relativeTypeName;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError("缺少分层类型: " + className, ex);
        }
    }

    private Class<?> requiredAdminClass(String relativeTypeName) {
        String className = ADMIN_ROOT_PACKAGE + "." + relativeTypeName;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError("缺少分层类型: " + className, ex);
        }
    }
}
