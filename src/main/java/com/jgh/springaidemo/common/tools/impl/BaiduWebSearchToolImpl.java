package com.jgh.springaidemo.common.tools.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jgh.springaidemo.common.tools.BaiduWebSearchTool;
import org.springframework.ai.tool.annotation.Tool;

import java.util.HashMap;
import java.util.Map;

public class BaiduWebSearchToolImpl implements BaiduWebSearchTool {

    private final String baiduApiKey = "bce-v3/ALTAK-X7gdZYxQR2Ut5Mm81BDgI/33f0aedf1ac73b18356bb97111f0e9aecb3a10c5";

    @Override
    @Tool(name = "baiduSearch", description = "百度在线搜索",returnDirect = false)
    public JSONObject search(String keyword) {

            String url = "https://qianfan.baidubce.com/v2/ai_search/chat/completions";

        JSONObject params = new JSONObject();
//        params.put("search_source","baidu_search_v2");
//        JSONArray resourceTypeFilter = new JSONArray();
//        JSONObject resourceItem = new JSONObject();
//        resourceItem.put("type","web");
//        resourceItem.put("top_k",20);
//        resourceTypeFilter.add(resourceItem);
//        params.put("resource_type_filter",resourceTypeFilter);
        JSONArray message = new JSONArray();
        JSONObject messageItem = new JSONObject();
        messageItem.put("role","user");
        messageItem.put("content",keyword);
        message.add(messageItem);
        params.put("messages",message);

        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        header.put("Authorization","Bearer " + this.baiduApiKey);

        HttpRequest request = HttpUtil.createPost(url).headerMap(header, true).body(params.toJSONString());
        try (HttpResponse execute = request.execute()){
            String body = execute.body();
            return JSONObject.parseObject(body);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


}
