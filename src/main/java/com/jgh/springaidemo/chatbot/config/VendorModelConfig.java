package com.jgh.springaidemo.chatbot.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class VendorModelConfig {

    private Map<String, Map<String, String>> vendors;


}