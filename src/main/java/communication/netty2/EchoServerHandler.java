package communication.netty2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	private static final AtomicInteger counter = new AtomicInteger();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel[" + ctx.channel().remoteAddress() + "] active,total[" + counter.incrementAndGet() + "]");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel[" + ctx.channel().remoteAddress() + "] inactive,total[" + counter.decrementAndGet() + "]" );
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf)msg;
		byte[] reqClient = new byte[buf.readableBytes()];
		buf.readBytes(reqClient);
		String body = new String(reqClient,"UTF-8");
		System.out.println("client info : " + body);

		ByteBuf reply = null;
		byte[] reqServer = "wan shang 6 dian ".getBytes();
		reply = Unpooled.buffer(reqServer.length);
		reply.writeBytes(reqServer);
		ctx.writeAndFlush(reply);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("function channelReadComplete !");
//		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}