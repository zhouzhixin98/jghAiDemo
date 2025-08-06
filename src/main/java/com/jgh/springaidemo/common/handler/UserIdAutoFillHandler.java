package com.jgh.springaidemo.common.handler;

import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mpe.autofill.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;

/**
 * 全局获取用户ID
 */
@Component
@RequiredArgsConstructor
public class UserIdAutoFillHandler implements AutoFillHandler<String> {

//    private final UserUtil userUtil;

    /**
     * @param object 当前操作的数据对象
     * @param clazz  当前操作的数据对象的class
     * @param field  当前操作的数据对象上的字段
     * @retur
     */
    @Override
    public String getVal(Object object, Class<?> clazz, Field field) {
        //todo 需修改为获取用户id的实际逻辑
        return "1";
    }
}