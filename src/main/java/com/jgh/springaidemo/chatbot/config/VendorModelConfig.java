package com.jgh.springaidemo.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "vendors")
public class VendorModelConfig {

    private Map<String, Map<String, String>> vendors;

    public Map<String, Map<String, String>> getVendors() {
        return vendors;
    }

    public void setVendors(Map<String, Map<String, String>> models) {
        this.vendors = models;
    }

}