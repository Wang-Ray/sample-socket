package wang.angi.sample.mina.keepalive;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaSocketServer {

	static Logger logger = LoggerFactory.getLogger(MinaSocketServer.class);

	/**
	 * 心跳间隔
	 */
	private static final int REQUEST_INTERVAL = 15;

	/**
	 * 心跳响应超时时间
	 */
	private static final int REQUEST_TIMEOUT = 5;

	public static void main(String[] args) throws Exception {
		// 创建一个非阻塞的Server端Socket,用NIO
		SocketAcceptor acceptor = new NioSocketAcceptor();
		// 创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		// 设定这个过滤器将一行一行(/r/n)的读取数据
		chain.addLast("protocolCodecFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));

		KeepAliveMessageFactoryImpl serverKeepAliveMessageFactoryImpl = new KeepAliveMessageFactoryImpl();
		serverKeepAliveMessageFactoryImpl.setHeartBeatRequestSent("ping");
		serverKeepAliveMessageFactoryImpl.setHeartBeatResponseReceived("pong");
		serverKeepAliveMessageFactoryImpl.setHeartBeatRequestReceived("ping");
		serverKeepAliveMessageFactoryImpl.setHeartBeatResponseSent("pong");
		KeepAliveFilter keepAliveFilter = new KeepAliveFilter(serverKeepAliveMessageFactoryImpl, IdleStatus.BOTH_IDLE,KeepAliveRequestTimeoutHandler.LOG);

		// 心跳间隔
		keepAliveFilter.setRequestInterval(REQUEST_INTERVAL);
		// 心跳响应超时时间,
		keepAliveFilter.setRequestTimeout(REQUEST_TIMEOUT);

		keepAliveFilter.setForwardEvent(true);

		chain.addLast("heartBeat", keepAliveFilter);

		// 设定服务器端的消息处理器:一个MinaServerHandler对象,
		acceptor.setHandler(new MinaServerHandler());
		// 服务器端绑定的端口
		int bindPort = 9988;
		// 绑定端口,启动服务器
		acceptor.bind(new InetSocketAddress(bindPort));
		logger.info("Mina Server is Listing on:= " + bindPort);
	}
}
