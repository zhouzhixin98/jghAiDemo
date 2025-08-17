package com.jgh.springaidemo.chatbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模型默认供应商
 */
@Data
public class ModelDefaultVendor {

    private Map<String, String> models;

}
