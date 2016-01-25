package com.allinpay.io.socket.netty;

import java.util.concurrent.Executors;

import com.allinpay.its.j8583.util.CodeUtil;
import com.allinpay.its.jmeter.test.JmeterTrans;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
//									.addLast(new LoggingHandler(LogLevel.DEBUG))
									.addLast(new AsciiLengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
									.addLast(new NettyClientHandler());
						}
					});

			// 发起异步连接操作
			final ChannelFuture f = b.connect(host, port).sync();
			
			for(int i =0;i<1;i++){
				new Thread(new Runnable(){

					public void run() {
						try {

							ByteBuf req;
							JmeterTrans jmeter = new JmeterTrans();
							// 循环发送
							while (true) {

								String body = new String(CodeUtil.hexString2Byte(jmeter.generateReqMsg(2003)),
										"iso-8859-1");
								body = NettyServerHandler.stuffString(body.length() + "", 4, true, '0') + body;
								req = Unpooled.copiedBuffer(body.getBytes("iso-8859-1"));
								f.channel().writeAndFlush(req);
								Thread.sleep(1);

							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
					
				}).start();
			}

			// 等待客户端链路关闭
			 f.channel().closeFuture().sync();

//			 Thread.sleep(2*1000);
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
//		new NettyClient().connect(port, "172.16.62.100");
		new NettyClient().connect(port, "172.16.1.38");
//		new NettyClient().connect(port, "192.168.103.13");
//		int i = 0;
//		while(i<200){
//			new NettyClient().connect(port, "localhost");
//			i++;
//		}
		
	}
}
