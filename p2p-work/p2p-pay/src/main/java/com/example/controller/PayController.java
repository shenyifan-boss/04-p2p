package com.example.controller;

import com.alipay.api.AlipayApiException;
import com.example.service.AlipayService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/7
 **/
@Controller
public class PayController {

    @Resource
    private AlipayService alipayService;

    /**
     * 统一收单
     */
    @RequestMapping("/alipay")
    @Transactional
    public  synchronized void alipayPagePay(HttpServletResponse response){
        PrintWriter pw = null;
        try {
            String form = alipayService.pagePay(39,"10");

            pw = response.getWriter();
            pw.println(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.flush();
        pw.close();
    }
    @RequestMapping("/alipay/notify")
    public void notifyUrl(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
            alipayService.alipayNotify(params);
        }


        PrintWriter writer = response.getWriter();
        writer.println("success");
        writer.flush();
        writer.close();
    }
}
