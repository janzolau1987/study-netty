package com.yohotao.study.netty.demo00;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 与Server端配置要对应上
 * 
 * @author liujianzhu
 * @date 2016年8月24日 下午4:07:02
 */
public class HelloClientInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//
		pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder",new StringDecoder());
		pipeline.addLast("encoder",new StringEncoder());
		//
		pipeline.addLast("handler",new HelloClientHandler());
	}
}
