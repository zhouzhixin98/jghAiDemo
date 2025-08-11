package com.jgh.springaidemo.chatbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("spring.ai.openai.volcengine")
@Data
public class VolcengineProperties {

    private String apiKey;

    private String baseUrl;

    private String completionsPath;

}
