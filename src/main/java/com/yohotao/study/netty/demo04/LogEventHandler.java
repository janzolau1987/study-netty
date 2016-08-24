package com.yohotao.study.netty.demo04;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息处理handler
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午10:47:49
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append(msg.getReceived());
		builder.append("[");
		builder.append(msg.getSource().toString());
		builder.append("] [");
		builder.append(msg.getLogfile());
		builder.append("] : ");
		builder.append(msg.getMsg());
		System.out.println(builder.toString());
	}

}
