package com.jgh.springaidemo.chatbot.strategy;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: 智能聊天实现工厂
 * @Version: 1.0
 */
@Slf4j
@Component
public class LlmServiceFactory {

    private final Map<String, LlmService> serviceMap = new HashMap<>();

    @Autowired
    public LlmServiceFactory(List<LlmService> agentServices) {
        for (LlmService service : agentServices) {
            serviceMap.put(service.getProviderType(), service);
            log.info("注册LLM服务: {} - {}", service.getProviderType(), service.getClass().getSimpleName());
        }
    }

    /**
     * 获取LLM服务
     */
    public LlmService getService(String providerType) {
        LlmService service = serviceMap.get(providerType);
        if (BeanUtil.isEmpty(service)) {
            throw new RuntimeException("不支持的LLM服务提供商: " + providerType);
        }
        return service;
    }

    /**
     * 获取可用的LLM服务
     */
    public LlmService getAvailableService(String providerType) {
        LlmService service = getService(providerType);
        if (!service.isAvailable()) {
            throw new RuntimeException("LLM服务不可用: " + providerType);
        }
        return service;
    }

    /**
     * 获取所有可用的服务提供商
     */
    public Map<String, LlmService> getAllAvailableServices() {
        Map<String, LlmService> availableServices = new HashMap<>();
        for (Map.Entry<String, LlmService> entry : serviceMap.entrySet()) {
            if (entry.getValue().isAvailable()) {
                availableServices.put(entry.getKey(), entry.getValue());
            }
        }
        return availableServices;
    }


}
