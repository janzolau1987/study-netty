package com.yohotao.study.netty.demo05;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 处理服务器端channel
 * 
 * @author liujianzhu
 * @date 2016年8月26日 下午3:52:32
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//默默丢弃收到的数据
		//((ByteBuf)msg).release();
		
		//常见做法如下
		ByteBuf in = (ByteBuf) msg;
		try{
			//while(in.isReadable()) {
				System.out.println(in.toString(CharsetUtil.US_ASCII));
			//}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	/**
	 * 在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。
	 * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
