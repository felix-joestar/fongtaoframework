package com.fongtaoframework.starter.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Duration.class)
@MappedJdbcTypes(JdbcType.BIGINT)
public class DurationSecondsTypeHandler extends BaseTypeHandler<Duration> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, Duration parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setLong(index, parameter.toSeconds());
    }

    @Override
    public Duration getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return duration(rs.getLong(columnName), rs.wasNull());
    }

    @Override
    public Duration getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return duration(rs.getLong(columnIndex), rs.wasNull());
    }

    @Override
    public Duration getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long seconds = cs.getLong(columnIndex);
        return duration(seconds, cs.wasNull());
    }

    private Duration duration(long seconds, boolean wasNull) {
        if (wasNull) {
            return null;
        }
        return Duration.ofSeconds(seconds);
    }
}
