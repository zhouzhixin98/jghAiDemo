package com.jgh.springaidemo.common.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-26
 * @Description: 文件解析工具类 - 支持URL输入
 * @Version: 1.0
 */
@Component
public class FileAnalysisUtil {

    private static ImageUtil staticImageUtil;

    @Autowired
    public void setStaticImageUtil(ImageUtil imageUtil) {
        staticImageUtil = imageUtil;
    }

    /**
     * 解析图片文件：jpg、png、jpeg 直接使用图片理解模型解析
     * @param fileUrl 图片文件的URL地址
     */
    public static List<Document> parsePicFile(String fileUrl) {
        try {
            if (!StringUtils.hasText(fileUrl)) {
                throw new IllegalArgumentException("文件URL不能为空");
            }

            // 验证URL格式并从URL获取文件名
            new URL(fileUrl); // 验证URL格式
            String fileName = getFileNameFromUrl(fileUrl);
            
            // 验证是否为支持的图片格式
            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
                throw new IllegalArgumentException("不支持的图片格式，仅支持 jpg、jpeg、png");
            }

            // 直接使用URL进行图片理解模型分析
            String analysisResult = staticImageUtil.imageAnalysis(fileUrl);
            
            // 创建Document对象
            Document document = new Document(analysisResult);
            document.getMetadata().put("source", fileUrl);
            document.getMetadata().put("type", "image");
            document.getMetadata().put("format", getFileExtension(fileName));
            document.getMetadata().put("fileName", fileName);
            
            return Collections.singletonList(document);
            
        } catch (Exception e) {
            throw new RuntimeException("解析图片文件失败: " + fileUrl, e);
        }
    }

    /**
     * 解析文档文件：doc、docx、xls、xlsx、txt 使用tika解析
     * @param fileUrl 文档文件的URL地址
     */
    public static List<Document> parseDocFile(String fileUrl) {
        try {
            if (!StringUtils.hasText(fileUrl)) {
                throw new IllegalArgumentException("文件URL不能为空");
            }

            // 验证URL格式并从URL获取文件名
            new URL(fileUrl); // 验证URL格式
            String fileName = getFileNameFromUrl(fileUrl);
            
            // 验证是否为支持的文档格式
            Set<String> supportedFormats = Set.of(".doc", ".docx", ".xls", ".xlsx", ".txt", ".pdf");
            boolean isSupported = supportedFormats.stream().anyMatch(fileName.toLowerCase()::endsWith);
            if (!isSupported) {
                throw new IllegalArgumentException("不支持的文档格式，仅支持 doc、docx、xls、xlsx、txt、pdf");
            }

            // 使用 DefaultResourceLoader 加载URL资源
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(fileUrl);
            TikaDocumentReader reader = new TikaDocumentReader(resource);
            
            List<Document> documents = reader.get();
            
            // 为每个文档添加额外的元数据
            for (Document doc : documents) {
                doc.getMetadata().put("originalUrl", fileUrl);
                doc.getMetadata().put("type", "document");
                doc.getMetadata().put("format", getFileExtension(fileName));
                doc.getMetadata().put("fileName", fileName);
                // 注意：无法从URL直接获取文件大小，需要额外的HTTP请求
            }
            
            return documents;
            
        } catch (Exception e) {
            throw new RuntimeException("解析文档文件失败: " + fileUrl, e);
        }
    }

    /**
     * 解析PDF文件：pdf、ppt、pptx 使用图片理解逐页分析
     * @param fileUrl PDF文件的URL地址
     */
    public static List<Document> parsePdfFile(String fileUrl) {
        try {
            if (!StringUtils.hasText(fileUrl)) {
                throw new IllegalArgumentException("文件URL不能为空");
            }

            // 验证URL格式并从URL获取文件名
            new URL(fileUrl); // 验证URL格式
            String fileName = getFileNameFromUrl(fileUrl);
            
            // 根据文件类型选择处理方式
            if (fileName.toLowerCase().endsWith(".pdf")) {
                return parsePdfWithImageAnalysis(fileUrl);
            } else if (fileName.toLowerCase().endsWith(".ppt") || fileName.toLowerCase().endsWith(".pptx")) {
                // PPT文件先用Tika提取文本，然后结合图片分析
                return parsePptFile(fileUrl);
            } else {
                throw new IllegalArgumentException("不支持的文件格式，仅支持 pdf、ppt、pptx");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("解析PDF/PPT文件失败: " + fileUrl, e);
        }
    }

    /**
     * 使用PDFBox将PDF转换为图片并逐页分析
     */
    private static List<Document> parsePdfWithImageAnalysis(String fileUrl) throws IOException, URISyntaxException, MalformedURLException {
        List<Document> documents = new ArrayList<>();
        
        // 从URL下载PDF文件到临时文件
        URL url = new URL(fileUrl);
        try (InputStream inputStream = url.openStream()) {
            // 创建临时文件
            File tempFile = File.createTempFile("pdf_temp_", ".pdf");
            tempFile.deleteOnExit(); // 程序退出时删除临时文件
            
            // 将URL内容写入临时文件
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            try (PDDocument document = Loader.loadPDF(tempFile)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                int pageCount = document.getNumberOfPages();
                
                for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                    // 将PDF页面渲染为图片
                    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
                    
                    // 直接使用BufferedImage进行图片理解模型分析
                    String pageAnalysis = staticImageUtil.imageAnalysis(bufferedImage);
                    
                    // 创建Document对象
                    Document pageDocument = new Document(pageAnalysis);
                    pageDocument.getMetadata().put("source", fileUrl);
                    pageDocument.getMetadata().put("type", "pdf_page");
                    pageDocument.getMetadata().put("pageNumber", String.valueOf(pageIndex + 1));
                    pageDocument.getMetadata().put("totalPages", String.valueOf(pageCount));
                    pageDocument.getMetadata().put("format", "pdf");
                    pageDocument.getMetadata().put("fileName", getFileNameFromUrl(fileUrl));
                    
                    documents.add(pageDocument);
                }
            }
        }
        
        return documents;
    }

    /**
     * 解析PPT文件（结合文本提取和图片分析）
     */
    private static List<Document> parsePptFile(String fileUrl) {
        try {
            // 使用 DefaultResourceLoader 加载URL资源
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(fileUrl);
            TikaDocumentReader reader = new TikaDocumentReader(resource);
            List<Document> textDocuments = reader.get();
            
            // 为文本文档添加元数据
            for (Document doc : textDocuments) {
                doc.getMetadata().put("originalUrl", fileUrl);
                doc.getMetadata().put("type", "presentation_text");
                doc.getMetadata().put("format", getFileExtension(getFileNameFromUrl(fileUrl)));
                doc.getMetadata().put("fileName", getFileNameFromUrl(fileUrl));
                doc.getMetadata().put("extractionMethod", "tika");
            }
            
            // TODO: 如果需要更详细的PPT页面分析，可以添加将PPT转换为图片的逻辑
            // 这需要额外的库如Apache POI来处理PPT到图片的转换
            
            return textDocuments;
            
        } catch (Exception e) {
            throw new RuntimeException("解析PPT文件失败: " + fileUrl, e);
        }
    }

    /**
     * 从URL中提取文件名
     */
    private static String getFileNameFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            if (path != null && path.contains("/")) {
                return path.substring(path.lastIndexOf("/") + 1);
            }
            return path != null ? path : "unknown";
        } catch (Exception e) {
            // 如果解析失败，尝试从查询参数中提取
            if (fileUrl.contains("?")) {
                String beforeQuery = fileUrl.substring(0, fileUrl.indexOf("?"));
                if (beforeQuery.contains("/")) {
                    return beforeQuery.substring(beforeQuery.lastIndexOf("/") + 1);
                }
            }
            return "unknown";
        }
    }



    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
