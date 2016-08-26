package com.yohotao.study.netty.demo02;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * TCP"拆包/半包问题"：
 * 基于流的传输比如 TCP/IP, 接收到数据是存在 socket 接收的 buffer 中。不幸的是，基于流的传输并不是
 * 一个数据包队列，而是一个字节队列。意味着，即使你发送了2个独立的数据包，操作系统也不会作为2个消息处理而仅
 * 仅是作为一连串的字节而言。因此这是不能保证你远程写入的数据就会准确地读取。
 * 
 * 可以实现个TimeDecoder的ChannelHandler专门用于数据拆分的问题
 * 
 * 源码说明：
 * 1、ByteToMessageDecoder是ChannelInboundHandler的一个实现类，它可以在处理数据拆分的问题上变得很简单。
 * 2、每当有新数据接收的时候，ByteToMessageDecoder都会调用decode(...)方法来处理内部累积缓冲。
 * 3、decode(...) 方法可以决定当累积缓冲里没有足够数据时可以往 out 对象里放任意数据。当有更多的数据被接收了 ByteToMessageDecoder会再一次调用 decode() 方法。
 * 4、如果在 decode() 方法里增加了一个对象到 out 对象里，这意味着解码器解码消息成功。
 * 5、ByteToMessageDecoder将会丢弃在累积缓冲里已经被读过的数据。
 * 
 * @author liujianzhu
 * @date 2016年8月26日 下午5:16:25
 */
public class TimeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < 4) {
			return;
		}
		out.add(in.readBytes(4));
	}

}
