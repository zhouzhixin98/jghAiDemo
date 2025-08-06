package com.jgh.springaidemo.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageConvertUtils {

    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(source -> {
                    T target = null;
                    try {
                        target = targetClass.getDeclaredConstructor().newInstance();
                        BeanUtils.copyProperties(source, target);
                    } catch (Exception e) {
                        throw new RuntimeException("对象转换失败", e);
                    }
                    return target;
                })
                .collect(Collectors.toList());
    }

    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass, Function<S, T> converter) {
        return sourceList.stream().map(converter).collect(Collectors.toList());
    }

    /**
     * 转换分页对象类型（自动属性复制）
     *
     * @param sourcePage  原始分页对象
     * @param targetClass 目标对象Class
     * @return 转换后的分页对象
     */
    public static <S, T> IPage<T> convertPage(IPage<S> sourcePage, Class<T> targetClass) {
        // 创建空的目标分页对象
        IPage<T> targetPage = new Page<>();
        // 复制分页基本信息（当前页/每页数量/总记录数等）
        BeanUtils.copyProperties(sourcePage, targetPage, "records");

        // 转换records列表：逐个对象属性复制
        List<T> targetRecords = sourcePage.getRecords().stream()
                .map(source -> {
                    try {
                        T target = targetClass.getDeclaredConstructor().newInstance();
                        BeanUtils.copyProperties(source, target);
                        return target;
                    } catch (Exception e) {
                        throw new RuntimeException("对象转换失败", e);
                    }
                })
                .collect(Collectors.toList());

        targetPage.setRecords(targetRecords);
        return targetPage;
    }

    /**
     * 支持自定义转换逻辑
     *
     * @param sourcePage 原始分页对象
     * @param converter  自定义转换函数
     * @return 转换后的分页对象
     */
    public static <S, T> IPage<T> convertPage(
            IPage<S> sourcePage,
            Function<S, T> converter) {

        IPage<T> targetPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, targetPage, "records");

        List<T> targetRecords = sourcePage.getRecords().stream()
                .map(converter)
                .collect(Collectors.toList());

        targetPage.setRecords(targetRecords);
        return targetPage;
    }

    /**
     * // 当VO需要特殊处理字段时 使用示例
     * Page<SysUtilsLabelVO> voPage = PageConvertUtils.convertPage(
     *         sysUtilsLabelPage,
     *         source -> {
     *             SysUtilsLabelVO vo = new SysUtilsLabelVO();
     *             BeanUtils.copyProperties(source, vo);
     *             vo.setCustomField(calculateCustomValue(source)); // 自定义处理逻辑
     *             return vo;
     *         }
     * );
     */

}