package com.allinpay.io.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.debug("exceptionCaught");
		// 释放资源
		logger.warn("Unexpected exception from downstream : " + cause.getMessage());
		ctx.fireExceptionCaught(cause);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelRegistered");
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelActive");
		ctx.fireChannelActive();
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
		String body = new String(req);
		System.out.println("The time server receive order : " + body);
		if("a".equals(body)){
			Thread.sleep(60*1000);
		}
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis())
				.toString() : "BAD ORDER";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete");
		ctx.flush();
//		ctx.channel().disconnect();
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
