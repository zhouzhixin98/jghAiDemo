package com.jgh.springaidemo.common.typehandler;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JSONObject.class)
public class JsonObjectTypeHandler extends BaseTypeHandler<JSONObject> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, 
                                   JSONObject parameter, JdbcType jdbcType) 
                                   throws SQLException {
        ps.setString(i, parameter.toJSONString()); // Java对象转数据库字符串
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) 
                                      throws SQLException {
        String json = rs.getString(columnName);
        return JSONObject.parseObject(json); // 数据库字符串转Java对象
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) 
                                      throws SQLException {
        String json = rs.getString(columnIndex);
        return JSONObject.parseObject(json);
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) 
                                      throws SQLException {
        String json = cs.getString(columnIndex);
        return JSONObject.parseObject(json);
    }
}