/**
 * 
 */
package com.ziasset.webmessage.constants;

/**
 * @author admin
 *
 */
public final class WSConstants {

	/**
	 * 连接服务端ws.endpoint的地址
	 */
	public static final String WS_ENDPOINT_URL = "/ws/endpoint";

	/**
	 * ws允许跨域访问
	 */
	public static final String WS_ALLOWE_DORIGINS = "*";
	
	
	/**
	 * ws前端订阅的地址前缀，广播模式
	 */
	public static final String BROKER_TOPIC = "/topic";
	
	/**
	 * ws前端订阅的地址前缀，用户模式
	 */
	public static final String BROKER_USER = "/user";

	/**
	 * ws前端往后端发送数据的前缀，ws.send()的地址前缀
	 */
	public static final String APPLICATION_DESTINATION_PREFIXES = "/ws-push";




}
