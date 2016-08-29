package com.yohotao.study.netty.demo08;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * IdleState : ALL_IDLE 一段时间内没有数据接收或者发送 READER_IDLE 一段时间内没有数据接收 WRITER_IDLE
 * 一段时间内没有数据发送
 * 
 * 在netty的timeout包下，主要类有： .IdleStateEvent 超时的事件 .IdleStateHandler 超时状态处理
 * .ReadTimeoutHandler 读超时状态处理 .WriteTimeoutHandler 写超时状态处理
 * 
 * @author liujianzhu
 * @date 2016年8月29日 下午5:03:17
 */
public class HeartbeatHandlerInitializer extends ChannelInitializer<Channel> {
	private static final int READ_IDLE_TIMEOUT = 4;// 读超时
	private static final int WRITE_IDLE_TIMEOUT = 5;// 写超时
	private static final int ALL_IDLE_TIMEOUT = 7;// 所有超时

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		//
		pipeline.addLast(new IdleStateHandler(READ_IDLE_TIMEOUT, 
					WRITE_IDLE_TIMEOUT, ALL_IDLE_TIMEOUT, TimeUnit.SECONDS)); //使用IdleStateHandler分别设置读写超时的时间
		pipeline.addLast(new HeartbeatServerHandler()); //用于处理超时时，发送心跳
	}
}
