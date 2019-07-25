package com.ziasset.webmessage.config;

import com.ziasset.webmessage.constants.WSConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

/**
 * @author zhouhahong
 */
@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 服务器要监听的端口，message会从这里进来，要对这里加一个Handler </br>
     * 这样在网页中就可以通过websocket连接上服务了</br>
     * Register STOMP endpoints mapping each to a specific URL and (optionally)
     * enabling and configuring SockJS fallback options.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry
                .addEndpoint(WSConstants.WS_ENDPOINT_URL)   /* 前段new SockJS('/endpointService')的地址*/
                // 注册stomp的端点
                .setAllowedOrigins(WSConstants.WS_ALLOWE_DORIGINS) // 允许跨域
                .setHandshakeHandler(new WsHandshakeHandler())//握手处理程序
                .addInterceptors(new WsHandshakeInterceptor());//添加握手拦截器

        registry.setErrorHandler(new StompSubProtocolErrorHandler());
    }

    /**
     * 配置broker </br>
     * 配置消息代理选项。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker(WSConstants.BROKER_TOPIC, WSConstants.BROKER_USER) // // stompClient.subscribe(url)
                // url的前缀 订阅Broker名称
                .setHeartbeatValue(new long[]{60 * 1000L, 60 * 1000L}) // 配置stomp协议里, server返回的心跳 60秒
                .setTaskScheduler(new DefaultManagedTaskScheduler());
        // 全局使用的消息前缀（客户端订阅路径上会体现出来）
        // 配置前缀, 有这些前缀的会被到有@SubscribeMapping与@MessageMapping的业务方法拦截
        registry.setApplicationDestinationPrefixes(WSConstants.APPLICATION_DESTINATION_PREFIXES);// 前段往后端发送数据的前缀，ws.send()的地址前缀
        // 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        // registry.setUserDestinationPrefix(WSConstants.BROKER_USER+"/"); //

    }

    /**
     * 消息传输参数配置 </br>
     * Configure options related to the processing of messages received from and
     * sent to WebSocket clients.
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry //
                .setSendTimeLimit(15 * 1000) // The default value is 10 seconds (i.e. 10 * 10000).
                .setSendBufferSizeLimit(512 * 1024) // The default value is 512K (i.e. 512 * 1024).
                .setMessageSizeLimit(128 * 1024); // The default value is 64K (i.e. 64 * 1024). 128k 消息大小
    }

    /**
     * 输入通道参数设置 </br>
     * Configure the {@link org.springframework.messaging.MessageChannel} used for
     * incoming messages from WebSocket clients. By default the channel is backed by
     * a thread pool of size 1. It is recommended to customize thread pool settings
     * for production use.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        int cpuSize = Runtime.getRuntime().availableProcessors();
        registration.taskExecutor() //
                .corePoolSize(cpuSize * 2) // 核心线程数
                .maxPoolSize(100) // 最大线程数
                .keepAliveSeconds(60) // 线程活动时间
                .queueCapacity(10000);// 队列容量

        /**
         * 为消息通道配置给定拦截器
         */
        registration.interceptors(new WsChannelInterceptor());

    }

    /**
     * 输出通道参数设置 </br>
     * Configure the {@link org.springframework.messaging.MessageChannel} used for
     * outbound messages to WebSocket clients. By default the channel is backed by a
     * thread pool of size 1. It is recommended to customize thread pool settings
     * for production use.
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        int cpuSize = Runtime.getRuntime().availableProcessors();

        registration.taskExecutor() //
                .corePoolSize(cpuSize * 2) // 核心线程数
                .maxPoolSize(200) // 最大线程数
                .keepAliveSeconds(60) // 线程活动时间
                .queueCapacity(20000);// 队列容量

        /**
         * 为消息通道配置给定拦截器
         */
        registration.interceptors(new WsChannelInterceptor());
    }

}
