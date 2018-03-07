package bhz.netty.serial;

import java.io.File;
import java.io.FileOutputStream;

import bhz.utils.GzipUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Req req = (Req)msg;
		System.out.println("Server : " + req.getId() + ", " + req.getName() + ", " + req.getRequestMessage());
		byte[] attachment = GzipUtils.ungzip(req.getAttachment());
		
		String path = System.getProperty("user.dir") + File.separatorChar + "receive" +  File.separatorChar + "00"+req.getId()+".jpg";
		System.out.println(path);
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(attachment);
        fos.close();
		Resp resp = new Resp();
		resp.setId(req.getId());
		resp.setName("resp" + req.getId());
		resp.setResponseMessage("响应内容" + req.getId());
		ctx.writeAndFlush(resp);//.addListener(ChannelFutureListener.CLOSE);
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		 ctx.flush();//.addListener(ChannelFutureListener.CLOSE);
		System.out.println("im server, over");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	
	
}
