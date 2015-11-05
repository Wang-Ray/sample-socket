package com.allinpay.io.socket.netty.jmeter.asciilengthfield;

import com.allinpay.io.socket.netty.AsciiLengthFieldBasedFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {

	public void bind(int port) throws Exception {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, bossGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).handler(new LoggingHandler(LogLevel.DEBUG))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
							.addLast(new LoggingHandler(LogLevel.DEBUG))
							        .addLast(new AsciiLengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
//									.addLast(new StringEncoder())
									.addLast(new NettyServerHandler());
						}
					});
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();

			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();

			// while(true){
			// Thread.sleep(20*1000);
			// f.channel().disconnect();
			// }

			// Thread.sleep(30*1000);
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
//			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 8099;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
		new NettyServer().bind(port);
	}
}
