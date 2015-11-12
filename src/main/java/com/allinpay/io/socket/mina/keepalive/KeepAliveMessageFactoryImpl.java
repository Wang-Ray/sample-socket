package com.allinpay.io.socket.mina.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳消息及处理
 * 
 * @author Angi
 *
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

	static Logger logger = LoggerFactory.getLogger(KeepAliveMessageFactoryImpl.class);

	private Object heartBeatRequest;
	private Object heartBeatResponse;

	/**
	 * 获取需要发送的心跳包。<br/>
	 * null则表明不需要发送请求心跳，<br/>
	 * 否则发送，配合requestTimeoutHandler（决定是否需要心跳响应或请求超时处理）和isResponse()
	 */
	@Override
	public Object getRequest(IoSession session) {
		logger.info("请求心跳包: " + heartBeatRequest);
		return heartBeatRequest;
	}

	/**
	 * 判断收到的是否是心跳响应，否则会执行TimeoutHandler（
	 * 除非超时异常handler是KeepAliveRequestTimeoutHandler.DEAF_SPEAKER）
	 */
	@Override
	public boolean isResponse(IoSession session, Object message) {
		logger.info("响应心跳包: " + message);
		if (message.equals(heartBeatResponse))
			return true;
		return false;
	}

	/**
	 * 判断收到的是否是心跳请求，可以配合getResponse()来决定是否需要发送心跳响应
	 */
	@Override
	public boolean isRequest(IoSession session, Object message) {
		logger.info("请求心跳包: " + message);
		if (message.equals(heartBeatRequest))
			return true;
		return false;
	}

	/**
	 * 获取需要响应的心跳包，null则表明不需要发送心跳响应
	 */
	@Override
	public Object getResponse(IoSession session, Object request) {
		logger.info("响应心跳包: " + heartBeatResponse);
		return heartBeatResponse;
	}

	public Object getHeartBeatRequest() {
		return heartBeatRequest;
	}

	public void setHeartBeatRequest(Object heartBeatRequest) {
		this.heartBeatRequest = heartBeatRequest;
	}

	public Object getHeartBeatResponse() {
		return heartBeatResponse;
	}

	public void setHeartBeatResponse(Object heartBeatResponse) {
		this.heartBeatResponse = heartBeatResponse;
	}

}
