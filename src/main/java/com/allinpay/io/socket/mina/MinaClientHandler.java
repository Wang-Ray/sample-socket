package com.allinpay.io.socket.mina;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaClientHandler extends IoHandlerAdapter {

	Logger logger = LoggerFactory.getLogger(MinaClientHandler.class);

	// 当一个连接建立时
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("connected:" + session.getRemoteAddress());

		// 连续发送两条消息，服务端一次收到
		WriteFuture wf1 = session.write("client says：我来啦1........");
		logger.debug("client says：我来啦1........");
		wf1.addListener(new IoFutureListener<IoFuture>() {
			public void operationComplete(IoFuture future) {
				logger.debug("future1 -- write completed!");
			}
		});

		Thread.sleep(10 * 1000);
		WriteFuture wf2 = session.write("client says：我来啦2........");
		logger.debug("client says：我来啦2........");
		wf2.addListener(new IoFutureListener<WriteFuture>() {
			public void operationComplete(WriteFuture future) {
				if (future.isWritten()) {
					logger.info("Message send:");
				} else {
					logger.error("发送失败：" + "，原因：" + future.getException());
				}
			}
		});

	}

	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("messageSent");
	}


	// 当一个连接关闭时
	@Override
	public void sessionClosed(IoSession session) {
		// 已经无法发送消息了，所以下面这句无效
		// session.write("client says：我走啦........");
		logger.debug("disconnect:" + session.getRemoteAddress());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.debug("connection error:" + session.getRemoteAddress());
	}

	// 当服务端发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为String:
		String s = (String) message;
		logger.debug(s);
		// 测试将消息回送给客户端
		// session.write(s);
	}
}