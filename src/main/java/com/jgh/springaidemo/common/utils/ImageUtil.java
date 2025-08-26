package com.jgh.springaidemo.common.utils;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.chat.MessageFormat;
import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import com.jgh.springaidemo.chatbot.enums.ChatModelType;
import com.jgh.springaidemo.chatbot.strategy.ChatClientFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import java.util.Base64;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-20
 * @Description: 图片识别工具类 -- 使用百炼平台 qwen-vl-ocr模型进行图片理解
 * @Version: 1.0
 */
@Component
public class ImageUtil {

    private final ChatClient dashScopeChatClient;

    private final String DEFAULT_MODEL = "qwen-vl-plus";

    public ImageUtil(ChatClientFactory chatClientFactory) {
        this.dashScopeChatClient = chatClientFactory.getChatClient(ChatModelType.DASH_SCOPE);
    }

    /**
     * 传入网络url进行图片分析
     * @param url 网络图片URL
     * @return 图片分析结果
     */
    public String imageAnalysis(String url) throws URISyntaxException, MalformedURLException {
        String systemPrompt = "你是一个图片识别及理解专家，你的任务是以最完善、最详细的角度去提取图片的信息，从而提供图片信息让我的llm拥有图片解析能力。解析内容尽可能完善以便应对用户各方面的提问。";
        String prompt = "请根据图片详细信息提取关键字。";
        List<Media> mediaList = List.of(new Media(MimeTypeUtils.IMAGE_PNG,
                new URI(url).toURL().toURI()));
        UserMessage message =
                UserMessage.builder().text(prompt).media(mediaList).metadata(new HashMap<>()).build();
        message.getMetadata().put(DashScopeApiConstants.MESSAGE_FORMAT, MessageFormat.IMAGE);

        return dashScopeChatClient.prompt(new Prompt(message,
                DashScopeChatOptions.builder().withModel(DEFAULT_MODEL).withMultiModel(true).build())).system(systemPrompt).call().content();
    }

    /**
     * 传入BufferedImage进行图片分析
     * @param bufferedImage 图片对象
     * @return 图片分析结果
     */
    public String imageAnalysis(BufferedImage bufferedImage) throws IOException, URISyntaxException {
        // 将BufferedImage转换为Base64编码的Data URI
        String dataUri = convertBufferedImageToDataUri(bufferedImage);
        
        String systemPrompt = "你是一个图片识别及理解专家，你的任务是以最完善、最详细的角度去提取图片的信息，从而提供图片信息让我的llm拥有图片解析能力。解析内容尽可能完善以便应对用户各方面的提问。";
        String prompt = "请根据图片详细信息提取关键字。";
        
        List<Media> mediaList = List.of(new Media(MimeTypeUtils.IMAGE_PNG,
                new URI(dataUri)));
        UserMessage message =
                UserMessage.builder().text(prompt).media(mediaList).metadata(new HashMap<>()).build();
        message.getMetadata().put(DashScopeApiConstants.MESSAGE_FORMAT, MessageFormat.IMAGE);

        return dashScopeChatClient.prompt(new Prompt(message,
                DashScopeChatOptions.builder().withModel(DEFAULT_MODEL).withMultiModel(true).build())).system(systemPrompt).call().content();
    }

    /**
     * 传入本地文件路径进行图片分析
     * @param filePath 本地图片文件路径
     * @return 图片分析结果
     */
    public String imageAnalysisFromFile(String filePath) throws IOException, URISyntaxException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }
        
        BufferedImage bufferedImage = ImageIO.read(file);
        if (bufferedImage == null) {
            throw new IOException("无法读取图片文件: " + filePath);
        }
        
        return imageAnalysis(bufferedImage);
    }

    /**
     * 将BufferedImage转换为Data URI格式
     * @param image BufferedImage对象
     * @return Data URI字符串
     */
    private String convertBufferedImageToDataUri(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.getEncoder().encodeToString(imageBytes);
        return "data:image/png;base64," + base64String;
    }



}
