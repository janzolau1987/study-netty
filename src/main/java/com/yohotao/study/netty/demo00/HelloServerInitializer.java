package com.yohotao.study.netty.demo00;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 创建和实现ChannelInitializer，增加ChannelHandler处理消息
 * 
 * @author liujianzhu
 * @date 2016年8月24日 下午3:53:55
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//以"\n"为结尾分割的解码器
		pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		
		//字符串解码和编码
		pipeline.addLast("decoder",new StringDecoder());
		pipeline.addLast("encoder",new StringEncoder());
		
		//自定义handler
		pipeline.addLast("handler",new HelloServerHandler());
	}
}
