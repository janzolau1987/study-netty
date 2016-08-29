package com.yohotao.study.netty.demo06;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("[TimeDecoder running...]");
		if(in.readableBytes() < 4) return;
		//
		out.add(new UnixTime(in.readUnsignedInt()));
	}

}
