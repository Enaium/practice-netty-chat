/**
 * Copyright (c) 2022 Enaium
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.enaium.chat.server.netty;

import cn.enaium.chat.common.message.Message;
import cn.enaium.chat.common.protocol.MessageCodec;
import cn.enaium.chat.server.netty.annotation.Handler;
import cn.enaium.chat.server.netty.handler.AbstractHandler;
import cn.enaium.chat.server.netty.util.SpringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Enaium
 */
@Component
public class NettyServer {

    private final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

    @Value("${chat.netty.port}")
    private int port;

    //Channel to Client
    public final ConcurrentHashMap<SocketChannel, Client> online = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Long, SocketChannel> onlineId = new ConcurrentHashMap<>();


    public Client getClient(SocketChannel channel) {
        return online.get(channel);
    }

    public SocketChannel getSocketChannel(long id) {
        return onlineId.get(id);
    }


    @Autowired
    private SpringUtil springUtil;

    public void startServer() {
        var boss = new NioEventLoopGroup();
        var worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    online.put(ch, new Client());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            var client = online.get((SocketChannel) ctx.channel());
                            online.remove((SocketChannel) ctx.channel());
                            onlineId.remove(client.getId());
                            super.channelInactive(ctx);
                        }
                    });
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 8, 4, 0, 0));
                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(new MessageCodec());

                    for (String s : springUtil.context.getBeanNamesForAnnotation(Handler.class)) {
                        ch.pipeline().addLast((AbstractHandler<? extends Message>) springUtil.context.getBean(s));
                    }
                }
            });
            var sync = serverBootstrap.bind(port).sync();
            System.out.println("NettyServer Port:" + port);
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
