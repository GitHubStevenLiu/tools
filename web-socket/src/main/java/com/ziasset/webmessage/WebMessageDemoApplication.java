package com.ziasset.webmessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouhahong
 */
@SpringBootApplication(scanBasePackages={"com.ziasset.*"})
@RestController
@EnableCaching
@EnableAsync
@Slf4j
public class WebMessageDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMessageDemoApplication.class, args);
    }


//    @Autowired
//    RedisUtil redisUtil;
    @Autowired
    private SimpMessagingTemplate template;


    /**
     * 同样的发送消息   只不过是ws版本  http请求不能访问
     * 根据用户key发送消息
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public String greeting2(String message) throws Exception {
        log.info("message={}", message);
        return "服务端收到客户端的消息，"+message;
    }

    /**
     * 向执行用户发送请求
     * @return
     */
    @RequestMapping(value = "send2user")
    @ResponseBody
    public int sendMq2User(@RequestParam("userId") String userId){
        // 根据用户名称获取用户对应的session id值
//        String wsSessionId = redisUtil.hget(RedisConstants.USER_WEB_SOCKET_ID_KEY, userId)+"";
//        System.out.println(wsSessionId);
        template.convertAndSendToUser(userId, "/msg", "用户：" + userId);
//        RequestMessage demoMQ = new RequestMessage();
//        demoMQ.setName(msg);
//
//        // 生成路由键值，生成规则如下: websocket订阅的目的地 + "-user" + websocket的sessionId值。生成值类似:
//        String routingKey = getTopicRoutingKey("demo", wsSessionId);
//        // 向amq.topi交换机发送消息，路由键为routingKey
//        log.info("向用户[{}]sessionId=[{}]，发送消息[{}]，路由键[{}]", name, wsSessionId, wsSessionId, routingKey);
//        amqpTemplate.convertAndSend("amq.topic", routingKey,  JSON.toJSONString(demoMQ));
        return 0;
    }
}
