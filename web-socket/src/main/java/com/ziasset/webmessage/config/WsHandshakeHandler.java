/**
 * 
 */
package com.ziasset.webmessage.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * @author admin
 *
 */
@Slf4j
public class WsHandshakeHandler extends DefaultHandshakeHandler {

    /**
     *
     * 将用户与WebSocket serssion 相关联的方法
     * A method that can be used to associate a user with the WebSocket session
     * in the process of being established. The default implementation calls
     * {@link ServerHttpRequest#getPrincipal()}
     * <p>Subclasses can provide custom logic for associating a user with a session,
     * for example for assigning a name to anonymous users (i.e. not fully authenticated).
     *
     * @param request    the handshake request
     * @param wsHandler  the WebSocket handler that will handle messages
     * @param attributes handshake attributes to pass to the WebSocket session
     * @return the user for the WebSocket session, or {@code null} if not available
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        HttpServletRequest re = ((ServletServerHttpRequest) request).getServletRequest();
        String userId = re.getParameter("userId");
//        RedisUtil redisUtil = SpringUtils.getBean(RedisUtil.class);
//        redisUtil.hset(RedisConstants.USER_WEB_SOCKET_ID_KEY, userId, re.getSession().getId());

        log.info("执行绑定用户与 webSocket session 关系");
        return super.determineUser(request, wsHandler, attributes);
    }


}
