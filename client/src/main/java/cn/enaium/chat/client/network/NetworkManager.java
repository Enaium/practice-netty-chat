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

package cn.enaium.chat.client.network;

import cn.enaium.chat.client.Chat;
import cn.enaium.chat.client.gui.screen.TitleScreen;
import cn.enaium.chat.client.util.OptionUtil;
import cn.enaium.chat.common.message.Message;
import cn.enaium.chat.common.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Enaium
 */
public class NetworkManager {

    public SocketChannel socketChannel;

    private final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

    private final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

    public Bootstrap create() {
        var messageCodec = new MessageCodec();
        var networkHandler = new NetworkHandler();

        return new Bootstrap().group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 8, 4, 0, 0));
                ch.pipeline().addLast(loggingHandler);
                ch.pipeline().addLast(messageCodec);
                ch.pipeline().addLast(networkHandler);
            }
        });
    }

    public void connect(Bootstrap bootstrap) {
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8008);
        channelFuture.addListener(future -> {
            if (future.cause() != null) {
                OptionUtil.error(future.cause());
                Chat.getInstance().setScreen(new TitleScreen());
            } else {
                socketChannel = (SocketChannel) channelFuture.channel();
            }
        });
        channelFuture.syncUninterruptibly();
    }

    public void disconnect() {
        nioEventLoopGroup.shutdownGracefully();
    }


    public void stop() throws InterruptedException {
        if (socketChannel != null) {
            socketChannel.close().sync();
        }
    }


    public void send(Message message) {
        socketChannel.writeAndFlush(message);
    }
}
