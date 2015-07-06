package com.allinpay.io.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient {

	public void connect(int port, String host) throws Exception {
		// 配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(new LoggingHandler(LogLevel.DEBUG))
									.addLast(new NettyClientHandler1(), new NettyClientHandler(),
											new NettyClientHandler2());
						}
					});

			// 发起异步连接操作
			ChannelFuture f = b.connect(host, port).sync();

			// 等待客户端链路关闭
			 f.channel().closeFuture().sync();

//			 Thread.sleep(10*1000);
//			 f.channel().close().sync();
			// f.channel().disconnect().sync();
			// Thread.sleep(30*1000);
		} finally {
			// 优雅退出，释放NIO线程组
			 group.shutdownGracefully();
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 8081;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
		new NettyClient().connect(port, "127.0.0.1");
	}
}
