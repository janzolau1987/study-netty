package com.yohotao.study.netty.demo06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimeClientHandler extends SimpleChannelInboundHandler<UnixTime> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, UnixTime msg) throws Exception {
		System.out.println("[TimeClientHandler running...]");
		System.out.println(msg);
	}
}
