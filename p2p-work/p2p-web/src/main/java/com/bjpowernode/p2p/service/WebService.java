package com.bjpowernode.p2p.service;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.common.Constants;
import com.bjpowernode.util.CommonUtil;
import com.bjpowernode.util.HttpClientUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/29
 **/
@Service
public class WebService {
    @Resource
    private RedisTemplate redisTemplate;

    /** 发送短信
     * @param phone 手机号
     * @return 验证码
     */
    public String sendCodeSMS(String phone){
        String code = null;
        String random = random(4);
        Map<String,String> map =new HashMap<>();
        map.put("mobile", phone);
        map.put("content", String.format(Constants.JDWX_SMS_CONTENT, random));
        map.put("appkey", Constants.JDWX_106SMS_APPKEY);
        try {
            //String json = HttpClientUtils.doGet(Constants.JDWX_106SMS_URL,map);
            String json = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 0,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-1111611</remainpoint>\\n <taskID>101609164</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                    "}";
            if(json!=null){
                JSONObject jsonObject = JSONObject.parseObject(json);
                String jdwxCode = jsonObject.getString("code");
                if ("10000".equals(jdwxCode)){
                    String xmlStr = jsonObject.getString("result");
                    if(xmlStr!=null){
                        //解析xml
                        String returnstatus = readXml(xmlStr);
                        if ("Success".equalsIgnoreCase(returnstatus)){
                            //存到redis
                            redisTemplate.opsForValue().set(phone, random, 300, TimeUnit.SECONDS);
                            code=  random;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            code=null;
        }
        return code;
    }

    //短信的内容，4位随机数
    private String random(int len){
        len = 4;
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<len;i++){
            int result = ThreadLocalRandom.current().nextInt(10);
            stringBuffer.append(result);
        }
        return stringBuffer.toString();
    }

    //解析xml,读取returnstatus
    private String readXml(String xml){
        String result=null;
        try {
            Document doc = DocumentHelper.parseText(xml);
            Node node = doc.selectSingleNode("//returnstatus");
            if(node!=null){
                result = node.getText()==null?null:node.getText().trim();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            result=null;
        }
        return result;
    }
    //验证短信验证码
    public boolean checkRedisVerificationCode(String phone , String code){
        boolean result=false;
        String str = (String) redisTemplate.opsForValue().get(phone);
        if (str!=null&&code!=null){
            if (str.equals(code)){
                result=true;
            }else {
                result=false;
            }
        }
        return result;
    }
    //注册成功后删除redis中的key
    public void delRedisKey(String phone){
        Boolean delete = redisTemplate.delete(phone);
    }

    /** 调用京东万象，验证身份是否正确
     * @param name  姓名
     * @param idCard 身份证号
     */
    public boolean registerRealName(String name,String idCard){
        boolean result=false;
        Map<String,String> map =new HashMap<>();
            map.put("cardNo", idCard);
            map.put("realName", name);
            map.put("appkey", Constants.JDWX_IDENTITY_APPKEY);
            try {
                String str="{\n" +
                        "    \"code\": \"10000\",\n" +
                        "    \"charge\": false,\n" +
                        "    \"remain\": 1305,\n" +
                        "    \"msg\": \"查询成功\",\n" +
                        "    \"result\": {\n" +
                        "        \"error_code\": 0,\n" +
                        "        \"reason\": \"成功\",\n" +
                        "        \"result\": {\n" +
                        "            \"realname\": \"乐天磊\",\n" +
                        "            \"idcard\": \"350721197702134399\",\n" +
                        "            \"isok\": true\n" +
                        "        }\n" +
                        "    }\n" +
                        "}";
                //String str = HttpClientUtils.doGet(Constants.JDWX_IDENTITY_URL, map);
                    JSONObject jsonObject=JSONObject.parseObject(str);
                    String code = jsonObject.getString("code");
                    if("10000".equals(code)){
                        //继续解析json
                         boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
                         if(isok==true){
                             result=true;
                         }
                    }

            } catch (Exception e) {
                e.printStackTrace();
                result=false;
            }

        return result;
    }
}
