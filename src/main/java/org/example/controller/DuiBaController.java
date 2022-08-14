package org.example.controller;


import cn.hutool.json.JSONString;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.param.CreditConsumeParams;
import org.example.param.CreditNotifyParams;
import org.example.result.CreditConsumeResult;
import org.example.util.CreditTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("test")
public class DuiBaController {
    // 兑吧商城的编号
    private static  final String APPKEY = "XXXXXXXXXXX";
    private static  final String APPSECRET ="XXXXXXXXXXXXXXXXX";

    @GetMapping("delPoint")
    public String delPoint(String s){
        log.info("测试：{}", APPKEY+APPSECRET);
        return APPKEY+APPSECRET;
    }

    // 获取免登录url
    @GetMapping("getUrl")
    public String getUrl(){
        CreditTool tool = new CreditTool(APPKEY,APPSECRET);
        String s = tool.buildAutoLoginRequest("12345", 1000L, null);
        return s;
    }

    @RequestMapping("/consume")
    @ResponseBody
    public String consume(HttpServletRequest request) {
        CreditTool tool = new CreditTool(APPKEY, APPSECRET);
        boolean success = false;
        String errorMessage = "";
        String bizId =null;
        Long credits=0L;
        try {
            CreditConsumeParams params = tool.parseCreditConsume(request);
            log.info("消费参数:{}", JSON.toJSONString(params));
            bizId = UUID.randomUUID()+""; //开发者业务订单号，保证唯一不重复
            credits = 1000L-params.getCredits(); // getCredits()是根据开发者自身业务，获取的用户最新剩余积分数。
            success = true;
        } catch (Exception e) {
            success = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
        CreditConsumeResult ccr = new CreditConsumeResult(success);
        ccr.setBizId(bizId);
        ccr.setErrorMessage(errorMessage);
        ccr.setCredits(credits);
        return ccr.toString();//返回扣积分结果json信息
    }

    @RequestMapping("result")
    public void result(HttpServletRequest request){
        /*
         *  兑换订单的结果通知请求的解析方法
         *  当兑换订单成功时，兑吧会发送请求通知开发者，兑换订单的结果为成功或者失败，如果为失败，开发者需要将积分返还给用户
         */
        CreditTool tool=new CreditTool(APPKEY, APPSECRET);

        try {
            CreditNotifyParams params= tool.parseCreditNotify(request);//利用tool来解析这个请求
            String orderNum=params.getOrderNum();
            if(params.isSuccess()){
                //兑换成功
                log.info("兑换成功：{}",JSON.toJSONString(params));
            }else{
                //兑换失败，根据orderNum，对用户的金币进行返还，回滚操作
                log.info("兑换失败：{}",JSON.toJSONString(params));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
