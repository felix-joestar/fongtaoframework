package com.fongtaoframework.starter.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fongtaoframework.starter.core.trace.TraceIdContext;
import com.fongtaoframework.starter.mybatis.domain.entity.AuditEntity;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import com.fongtaoframework.starter.mybatis.logging.P6spySqlFormat;
import com.fongtaoframework.starter.mybatis.typehandler.CommaSeparatedStringListTypeHandler;
import com.fongtaoframework.starter.mybatis.typehandler.CommaSeparatedStringSetTypeHandler;
import com.fongtaoframework.starter.mybatis.typehandler.DurationSecondsTypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MybatisSupportTypesTest {

    @AfterEach
    void clearTraceId() {
        TraceIdContext.clear();
    }

    @Test
    void shouldDefineAuditAndCrudEntityAnnotations() throws Exception {
        TableField createTime = AuditEntity.class.getDeclaredField("createTime").getAnnotation(TableField.class);
        TableField updateTime = AuditEntity.class.getDeclaredField("updateTime").getAnnotation(TableField.class);

        assertThat(createTime.value()).isEqualTo("create_time");
        assertThat(createTime.fill()).isEqualTo(FieldFill.INSERT);
        assertThat(createTime.updateStrategy()).isEqualTo(FieldStrategy.NEVER);
        assertThat(updateTime.value()).isEqualTo("update_time");
        assertThat(updateTime.fill()).isEqualTo(FieldFill.INSERT_UPDATE);
        assertThat(CrudEntity.class.getDeclaredField("version").getAnnotation(Version.class)).isNotNull();
        assertThat(CrudEntity.class.getDeclaredField("deleted").getAnnotation(TableLogic.class)).isNotNull();
    }

    @Test
    void shouldMapCommaSeparatedListAndSet() throws Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        CallableStatement callableStatement = mock(CallableStatement.class);
        when(resultSet.getString("items")).thenReturn("a,b,c");
        when(resultSet.getString(1)).thenReturn("a,b,c");
        when(callableStatement.getString(1)).thenReturn("a,b,c");

        CommaSeparatedStringListTypeHandler listHandler = new CommaSeparatedStringListTypeHandler();
        listHandler.setNonNullParameter(preparedStatement, 1, List.of("a", "b", "c"), JdbcType.VARCHAR);
        assertThat(listHandler.getNullableResult(resultSet, "items")).containsExactly("a", "b", "c");
        assertThat(listHandler.getNullableResult(resultSet, 1)).containsExactly("a", "b", "c");
        assertThat(listHandler.getNullableResult(callableStatement, 1)).containsExactly("a", "b", "c");
        verify(preparedStatement).setString(1, "a,b,c");

        CommaSeparatedStringSetTypeHandler setHandler = new CommaSeparatedStringSetTypeHandler();
        assertThat(setHandler.getNullableResult(resultSet, "items")).containsExactlyInAnyOrder("a", "b", "c");
    }

    @Test
    void shouldMapDurationAsSeconds() throws Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("duration")).thenReturn(90L);
        when(resultSet.wasNull()).thenReturn(false);

        DurationSecondsTypeHandler handler = new DurationSecondsTypeHandler();
        handler.setNonNullParameter(preparedStatement, 1, Duration.ofSeconds(90), JdbcType.BIGINT);

        verify(preparedStatement).setLong(1, 90L);
        assertThat(handler.getNullableResult(resultSet, "duration")).isEqualTo(Duration.ofSeconds(90));
    }

    @Test
    void shouldFormatP6spySqlWithTraceIdAndSingleLineSql() {
        TraceIdContext.set("trace-001");

        P6spySqlFormat format = new P6spySqlFormat();

        assertThat(format.formatMessage(1, "now", 2, "statement", "", "", "jdbc:p6spy"))
                .isEmpty();
        assertThat(format.formatMessage(1, "now", 12, "statement", "select ?", "select 1\nfrom dual", "jdbc:p6spy"))
                .contains("traceId=trace-001")
                .contains("connectionId=1")
                .contains("category=statement")
                .contains("elapsedMs=12")
                .doesNotContain("\n");
    }
}
