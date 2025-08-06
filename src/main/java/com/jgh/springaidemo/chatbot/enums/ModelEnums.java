package com.jgh.springaidemo.chatbot.enums;

import lombok.Getter;

@Getter
public enum ModelEnums {

    QWEN("qwen", "Qwen3系列Turbo模型，实现思考模式和非思考模式的有效融合，可在对话中切换模式。推理能力以更小参数规模比肩QwQ-32B、通用能力显著超过Qwen2.5-Turbo，达到同规模业界SOTA水平", "", "qwen-turbo"),
    DEEPSEEK_R1("deepseek-r1","DeepSeek-R1 在后训练阶段大规模使用了强化学习技术，在仅有极少标注数据的情况下，极大提升了模型推理能力。在数学、代码、自然语言推理等任务上，性能较高，能力较强。", "dashscope", "deepseek-r1"),
    DEEPSEEK_V3("deepseek-v3", "DeepSeek-V3 为自研 MoE 模型，671B 参数，激活 37B，在 14.8T token 上进行了预训练，在长文本、代码、数学、百科、中文 能力上表现优秀。","dashscope", "deepseek-v3"),
    DOUBAO("doubao","Doubao-Seed-1.6全新多模态深度思考模型，同时支持auto/thinking/non-thinking三种思考模式。 non-thinking模式下，模型效果对比Doubao-1.5-pro/250115大幅提升。支持 256k 上下文窗口，输出长度支持最大 16k tokens。","volcengine","doubao-seed-1-6-250615"),
    KIMI("kimi","Kimi 系列模型是由月之暗面公司（Moonshot AI）推出的大语言模型。Kimi K2 是混合专家（MoE）语言模型，拥有1 万亿总参数和 320 亿激活参数 。Kimi K2 在前沿知识、推理和编码任务中表现出卓越性能，同时针对 Agent 能力进行了精心优化。","dashscope","Moonshot-Kimi-K2-Instruct")


    ;
    private String model;
    private String description;
    private String vendor;
    private String modelCode;

    public static ModelEnums getByModel(String model) {
        for (ModelEnums value : ModelEnums.values()) {
            if (value.model.equals(model)) {
                return value;
            }
        }
        return null;
    }

    ModelEnums(String model, String description, String vendor, String modelCode) {
        this.model = model;
        this.description = description;
        this.vendor = vendor;
        this.modelCode = modelCode;
    }
}
