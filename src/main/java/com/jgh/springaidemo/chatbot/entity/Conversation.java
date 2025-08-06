package com.jgh.springaidemo.chatbot.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;

import java.time.LocalDateTime;

/**
 * 对话实体类
 * @author JiuGHim
 */
@Data
@Accessors(chain = true)
@TableName("ai_chat_conversation")
@AutoTable(value = "ai_chat_conversation")
public class Conversation extends BaseEntity {
    
    /**
     * 对话ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @AutoColumn(comment = "唯一标识",type = MysqlTypeConstant.VARCHAR,length = 32)
    private String id;
    
    /**
     * 对话标题
     */
    @AutoColumn(comment = "对话标题",type = MysqlTypeConstant.VARCHAR,length = 255)
    private String title;
    
    /**
     * 用户ID
     */
    @AutoColumn(comment = "用户ID",type = MysqlTypeConstant.VARCHAR,length = 40)
    private String uid;
    @AutoColumn(comment = "用户ID",type = MysqlTypeConstant.BIGINT)
    private Long userId;
    
    /**
     * 当前使用的模型类型
     */
//    @AutoColumn(comment = "模型类型",type = MysqlTypeConstant.VARCHAR,length = 40)
//    private String modelType;
    
    /**
     * 当前使用的模型名称
     */
    @AutoColumn(comment = "模型名称",type = MysqlTypeConstant.VARCHAR,length = 50)
    private String modelName;

    /**
     * 厂商
     */
    @AutoColumn(comment = "厂商",type = MysqlTypeConstant.VARCHAR,length = 50)
    private String vendor;

    /**
     * 对话状态：active-活跃，archived-归档，deleted-删除
     */
    @AutoColumn(comment = "对话状态，active-活跃，archived-归档，deleted-删除",type = MysqlTypeConstant.VARCHAR,length = 20)
    private String conversationsStatus;
    
    /**
     * 系统提示词
     */
    @AutoColumn(comment = "系统提示词",type = MysqlTypeConstant.TEXT)
    private String systemPrompt;
    
    /**
     * 对话配置（JSON格式存储温度、最大token等参数）
     */
    @AutoColumn(comment = "对话配置（JSON格式存储温度、最大token等参数）",type = MysqlTypeConstant.JSON)
    private JSONObject config;
    
    /**
     * 消息总数
     */
    @AutoColumn(comment = "消息总数",type = MysqlTypeConstant.INT)
    private Integer messageCount;
    
    /**
     * 最后一条消息时间
     */
    @AutoColumn(comment = "最后消息时间",type = MysqlTypeConstant.TIMESTAMP)
    private LocalDateTime lastMessageTime;

}