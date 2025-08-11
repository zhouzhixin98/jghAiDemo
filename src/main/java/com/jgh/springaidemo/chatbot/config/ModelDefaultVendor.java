package com.jgh.springaidemo.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模型默认供应商
 */
@Component
@ConfigurationProperties(prefix = "models")
public class ModelDefaultVendor {

    private Map<String, String> models;

    public Map<String, String> getModels() {
        return models;
    }

    public void setModels(Map<String, String> models) {
        this.models = models;
    }
}
