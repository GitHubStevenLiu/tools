/**
 * 
 */
package com.ziasset.webmessage.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * @author yjq </br>
 *  ws接受消息，发送消息的前后的拦截器
 *
 */
@Slf4j
public class WsChannelInterceptor implements ChannelInterceptor {

	/**
	 * Invoked before the Message is actually sent to the channel. This allows for
	 * modification of the Message if necessary. If this method returns {@code null}
	 * then the actual send invocation will not occur.
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		SimpMessageHeaderAccessor accessor = getSimpMessageHeaderAccessor(message);
		switch (accessor.getMessageType()){
			case HEARTBEAT:
				log.info("心跳");
				break;
			case CONNECT_ACK:
				log.info("连接确认");
				break;
			case DISCONNECT_ACK:
				log.info("断开连接确认");
				break;
			case CONNECT:
				log.info("用户连接");
				break;
			case DISCONNECT:
				log.info("用户断开连接");
				break;
			case SUBSCRIBE:
				log.info("用户订阅");
				break;
			case UNSUBSCRIBE:
				log.info("用户解订");
				break;
			case MESSAGE:
				log.info("用户发送消息");
				break;
		}
		return message;
	}


	private SimpMessageHeaderAccessor getSimpMessageHeaderAccessor(Message<?> message) {
		SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
				SimpMessageHeaderAccessor.class);

		if (accessor == null) {
			log.info("preSend...获取不到StompHeaderAccessor");
			accessor = SimpMessageHeaderAccessor.wrap(message);
		}

		return accessor;
	}



}
