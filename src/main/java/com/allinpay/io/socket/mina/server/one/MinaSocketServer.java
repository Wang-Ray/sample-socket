package com.allinpay.io.socket.mina.server.one;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个端口仅接收一个连接
 * 
 * @author Angi
 *
 */
public class MinaSocketServer {

	static Logger logger = LoggerFactory.getLogger(MinaSocketServer.class);

	// 服务器端绑定的端口
	private int bindPort = 9988;
	private SocketAddress socketAddress = new InetSocketAddress(bindPort);
	// 创建一个非阻塞的Server端Socket,用NIO
	private SocketAcceptor acceptor = new NioSocketAcceptor();

	public void init() throws Exception {

		// 创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("oneConnection", new IoFilterAdapter() {
			public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
				unBind();
			}

			public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
				doBind();
			}
		});
		// 设定这个过滤器将一行一行(/r/n)的读取数据
		chain.addLast("protocolCodecFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));
		// 设定服务器端的消息处理器:一个MinaServerHandler对象,
		acceptor.setHandler(new MinaServerHandler());

		doBind();
	}

	protected void doBind() {
		// 绑定端口,启动服务器
		try {
			acceptor.bind(socketAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Mina Server is Listing on:= " + bindPort);
	}

	protected void unBind() {
		logger.debug("stop listen");
		// 设置为false，不然会把已经建立的连接断掉
		acceptor.setCloseOnDeactivation(false);
		acceptor.unbind(socketAddress);
	}

}
