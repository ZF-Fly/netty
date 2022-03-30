package com.netty.bootstrap.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(AbstractHandler.class);

    private Handler handler;

    public AbstractHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame){
            System.out.println("TextWebSocketFrame"+msg);
            textMessage(ctx,(TextWebSocketFrame)msg);
        }else if (msg instanceof WebSocketFrame){
            System.out.println("WebSocketFrame"+msg);
            webMessage(ctx,(WebSocketFrame)msg);
        }else if (msg instanceof FullHttpRequest){
            System.out.println("FullHttpRequest"+msg);
            httpMessage(ctx, (FullHttpRequest)msg);
        }
    }

    protected abstract void webMessage(ChannelHandlerContext ctx, WebSocketFrame msg);

    protected abstract void textMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg);

    protected abstract void httpMessage(ChannelHandlerContext ctx, FullHttpRequest msg);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        log.info(LogConstant.CHANNELINACTIVE+ctx.channel().localAddress().toString()+LogConstant.CLOSE_SUCCESS);
        handler.close(ctx.channel());
    }
}
