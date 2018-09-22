package com.netty.test;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static  final List<Channel> list = new ArrayList<Channel>();
    public static final HashMap<String,ChannelDomain> key = new HashMap<String,ChannelDomain>();
    public static final HashMap value = new HashMap<String,String>();

    // 处理从客户端发来的消息，核心方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel incoming = ctx.channel();
        ChannelDomain channelDomain = key.get(incoming.id().asLongText());
        if(channelDomain!=null&&channelDomain.getRemote()!=null){
            channelDomain.getRemote().writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
        }else{
            System.out.println("没有聊天的人");
        }
//        for (Channel channel : channels) {
//            if (channel != incoming) {
//                channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
//            } else {
//                channel.writeAndFlush(new TextWebSocketFrame("[本地发送]：" + msg.text()));
//                // Console打印，可以删除
//                StringBuffer sb = new StringBuffer();
//                sb.append(incoming.remoteAddress()).append("->").append(msg.text());
//                System.out.println(sb.toString());
//            }
//        }
    }

    // 有新通道加入的时候响应
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        ChannelDomain cd = new ChannelDomain();
        cd.setLocal(incoming);
        cd.setLocalStr(incoming.id().asLongText());
        cd.setIndex(list.size());
        if(!list.isEmpty()&&list.size()%2==1){
            Channel channel = list.get(list.size() - 1);
            cd.setRemote(channel);
            cd.setRemoteStr(channel.id().asLongText());

            ChannelDomain cds = new ChannelDomain();
            cds.setRemote(incoming);
            cds.setRemoteStr(incoming.id().asLongText());
            cds.setLocal(channel);
            cds.setLocalStr(channel.id().asLongText());
            cds.setIndex(channels.size());
            key.put(incoming.id().asLongText(),cd);
            key.put(channel.id().asLongText(),cds);
        }
        list.add(incoming);
        System.out.println(list.size());
        System.out.println(incoming.toString());
        System.out.println(key.toString());

//        for (Channel channel : channels) {
////            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
////        }

        channels.add(ctx.channel());
        System.out.println("Client:" + incoming.remoteAddress() + "加入");
    }

    // 有通道关闭时响应
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
        }
        System.out.println("Client:" + incoming.remoteAddress() + "离开");
        channels.remove(ctx.channel());
    }

    // 通道激活时响应
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();


//        list.add(incoming);
        System.out.println("Client:" + incoming.remoteAddress() + "在线");
    }

    // 通道关闭时响应
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "掉线");
    }

    // 异常时响应
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "异常");

        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}

