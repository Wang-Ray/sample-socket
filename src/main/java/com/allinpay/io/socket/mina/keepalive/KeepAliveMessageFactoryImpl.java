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

	/**
	 * 发送心跳请求消息
	 */
	private Object heartBeatRequestSent;
	/**
	 * 接收心跳响应消息
	 */
	private Object heartBeatResponseReceived;

	/**
	 * 接收心跳请求消息
	 */
	private Object heartBeatRequestReceived;
	/**
	 * 发送心跳响应消息
	 * 
	 */
	private Object heartBeatResponseSent;

	/**
	 * 获取需要发送的心跳包。<br/>
	 * null则表明不需要发送请求心跳，<br/>
	 * 否则发送，配合requestTimeoutHandler（决定是否需要心跳响应或请求超时处理）和isResponse()
	 */
	@Override
	public Object getRequest(IoSession session) {
		logger.info("发送心跳请求消息: " + heartBeatRequestSent);
		return heartBeatRequestSent;
	}

	/**
	 * 判断收到的是否是心跳响应，否则会执行TimeoutHandler（
	 * 除非超时异常handler是KeepAliveRequestTimeoutHandler.DEAF_SPEAKER）
	 */
	@Override
	public boolean isResponse(IoSession session, Object message) {
		logger.info("接收心跳响应消息: " + message);
		if (message.equals(heartBeatResponseReceived))
			return true;
		return false;
	}

	/**
	 * 判断收到的是否是心跳请求，可以配合getResponse()来决定是否需要发送心跳响应
	 */
	@Override
	public boolean isRequest(IoSession session, Object message) {
		logger.info("接收心跳请求消息: " + message);
		if (message.equals(heartBeatRequestReceived))
			return true;
		return false;
	}

	/**
	 * 获取需要响应的心跳包，null则表明不需要发送心跳响应
	 */
	@Override
	public Object getResponse(IoSession session, Object request) {
		logger.info("发送心跳响应消息: " + heartBeatResponseSent);
		return heartBeatResponseSent;
	}

	public Object getHeartBeatRequestSent() {
		return heartBeatRequestSent;
	}

	public void setHeartBeatRequestSent(Object heartBeatRequestSent) {
		this.heartBeatRequestSent = heartBeatRequestSent;
	}

	public Object getHeartBeatResponseReceived() {
		return heartBeatResponseReceived;
	}

	public void setHeartBeatResponseReceived(Object heartBeatResponseReceived) {
		this.heartBeatResponseReceived = heartBeatResponseReceived;
	}

	public Object getHeartBeatRequestReceived() {
		return heartBeatRequestReceived;
	}

	public void setHeartBeatRequestReceived(Object heartBeatRequestReceived) {
		this.heartBeatRequestReceived = heartBeatRequestReceived;
	}

	public Object getHeartBeatResponseSent() {
		return heartBeatResponseSent;
	}

	public void setHeartBeatResponseSent(Object heartBeatResponseSent) {
		this.heartBeatResponseSent = heartBeatResponseSent;
	}

}
