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
		// logger.debug("channelRead");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req);
		logger.info("server received: " + body);
		// if("a".equals(body)){
		// Thread.sleep(60*1000);
		// }
		// String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new
		// java.util.Date(System.currentTimeMillis())
		// .toString() : "BAD ORDER";

		ByteBuf resp = Unpooled.copiedBuffer((stuffString(body.length() + "", 4, true, '0') + body).getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete");
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

	/**
	 * 把字符串src填充到len长度，填充的字符串为padding，填充方向为：当stuffHead为true时 填充到src头部，否则填充到尾部.
	 * 
	 * @param src
	 * @param len
	 * @param stuffHead
	 * @param padding
	 */
	public static String stuffString(String src, int len, boolean stuffHead, char padding) {
		if (len <= 0) {
			return src;
		}
		if (null == src) {
			src = "";
		}
		int srcLen = src.length();
		StringBuffer buf = new StringBuffer(len);
		int paddingLen = len - srcLen;
		for (int i = 0; i < paddingLen; i++) {
			buf.append(padding);
		}
		if (stuffHead) {
			buf.append(src);
		} else {
			buf.insert(0, src);
		}
		return buf.toString();
	}
}
