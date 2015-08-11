package com.allinpay.io.socket.mina.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerKeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

	static Logger logger = LoggerFactory.getLogger(MinaSocketServer.class);

	private static final String HEART_BEAT_REQUEST = "ping";
	private static final String HEART_BEAT_RESPONSE = "pong";

	/**
	 * 获取需要发送的心跳包，可以配合isResponse()来决定是否收到心跳响应，null则表明不需要发送请求心跳
	 */
	@Override
	public Object getRequest(IoSession session) {
		logger.info("请求预设信息: " + HEART_BEAT_REQUEST);
		/** 返回预设语句 */
		return HEART_BEAT_REQUEST;
		// return null;
	}

	/**
	 * 判断收到的是否是心跳响应，否则会执行超时异常handler（
	 * 除非超时异常handler是KeepAliveRequestTimeoutHandler.DEAF_SPEAKER）
	 */
	@Override
	public boolean isResponse(IoSession session, Object message) {
		logger.info("响应心跳包信息: " + message);
		if (message.equals(HEART_BEAT_RESPONSE))
			return true;
		return false;
	}

	/**
	 * 判断收到的是否是心跳请求，可以配合getResponse()来决定是否需要发送心跳响应
	 */
	@Override
	public boolean isRequest(IoSession session, Object message) {
		logger.info("请求心跳包信息: " + message);
		if (message.equals(HEART_BEAT_REQUEST))
			return true;
		return false;
	}

	/**
	 * 获取需要响应的心跳包，null则表明不需要发送心跳响应
	 */
	@Override
	public Object getResponse(IoSession session, Object request) {
		logger.info("响应预设信息: " + HEART_BEAT_RESPONSE);
		/** 返回预设语句 */
		return HEART_BEAT_RESPONSE;
		// return null;
	}

}
