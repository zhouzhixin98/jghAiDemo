package com.jgh.springaidemo.common.typehandler;//package com.fyxt.runtime.config.typehandler;
//
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedTypes;
//import java.sql.*;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@MappedTypes(List.class)
//public class StringListTypeHandler extends BaseTypeHandler<List<String>> {
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i,
//                                   List<String> parameter, JdbcType jdbcType)
//                                   throws SQLException {
//        // 将List转换为逗号分隔的字符串
//        String value = String.join(",", parameter);
//        ps.setString(i, value);
//    }
//
//    @Override
//    public List<String> getNullableResult(ResultSet rs, String columnName)
//                                         throws SQLException {
//        return parseStringToList(rs.getString(columnName));
//    }
//
//    @Override
//    public List<String> getNullableResult(ResultSet rs, int columnIndex)
//                                         throws SQLException {
//        return parseStringToList(rs.getString(columnIndex));
//    }
//
//    @Override
//    public List<String> getNullableResult(CallableStatement cs, int columnIndex)
//                                         throws SQLException {
//        return parseStringToList(cs.getString(columnIndex));
//    }
//
//    private List<String> parseStringToList(String value) {
//        if (value == null || value.isEmpty()) {
//            return Collections.emptyList();
//        }
//        // 分割字符串并转换为List
//        return Arrays.asList(value.split(","));
//    }
//}