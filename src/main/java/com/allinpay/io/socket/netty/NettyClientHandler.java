package com.allinpay.io.socket.netty;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandler.Skip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

//	public static final byte[] req = "0016QUERY TIME ORDER".getBytes();
	private ByteBuf firstMessage;

	/**
	 * Creates a client-side handler.
	 */
	public NettyClientHandler() {
		byte[] req = "0016QUERY TIME ORDER".getBytes();
		firstMessage = Unpooled.buffer(req.length);
		// firstMessage.writeShort(16);
		firstMessage.writeBytes(req);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.debug("exceptionCaught");
		// 释放资源
		logger.warn("Unexpected exception from downstream : " + cause);
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelRegistered");
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.debug("channelActive");
//		ctx.writeAndFlush(firstMessage);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive");
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("channelRead");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("Now is : " + body);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete");
		// 继续传递
		ctx.fireChannelReadComplete();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.debug("userEventTriggered");
		ctx.fireUserEventTriggered(evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelWritabilityChanged");
		ctx.fireChannelWritabilityChanged();
	}

	@Override
	@Skip
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		logger.debug("connect");
		ctx.connect(remoteAddress, localAddress, promise);

	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.debug("disconnect");
		ctx.disconnect(promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.debug("close");
		ctx.close(promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.debug("flush");
		ctx.flush();
	}

}
