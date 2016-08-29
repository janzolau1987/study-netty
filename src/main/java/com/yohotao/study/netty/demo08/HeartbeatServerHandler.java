package com.yohotao.study.netty.demo08;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
	//定义心跳时要发送的内容
	final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",CharsetUtil.UTF_8));
	
	/**
	 * 判断是否是IdleStateEvent事件，是则处理
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if(event.state() == IdleState.READER_IDLE) 
				type = "read idle";
			else if(event.state() == IdleState.WRITER_IDLE)
				type = "write idle";
			else if(event.state() == IdleState.ALL_IDLE)
				type = "all idle";
			//
			ChannelFuture f = ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
			f.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			
			System.out.println(ctx.channel().remoteAddress() + "超时类型 : " + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
