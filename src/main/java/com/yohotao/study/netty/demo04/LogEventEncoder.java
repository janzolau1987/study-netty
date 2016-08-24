package com.yohotao.study.netty.demo04;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * 自定义ChannelHandler，实现从LogEvent消息编程为DatagramPacket消息
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午10:06:42
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
	private final InetSocketAddress remoteAddress;
	
	public LogEventEncoder(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LogEvent msg, List<Object> out) throws Exception {
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeBytes(msg.getLogfile().getBytes(CharsetUtil.UTF_8));
		buf.writeByte(LogEvent.SEPARATOR);
		buf.writeBytes(msg.getMsg().getBytes(CharsetUtil.UTF_8));
		out.add(new DatagramPacket(buf, remoteAddress));
	}

}
