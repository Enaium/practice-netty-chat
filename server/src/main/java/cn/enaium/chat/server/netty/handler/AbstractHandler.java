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

package cn.enaium.chat.server.netty.handler;

import cn.enaium.chat.common.message.Message;
import cn.enaium.chat.common.message.response.StringResponse;
import cn.enaium.chat.server.netty.NettyServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Enaium
 */
@ChannelHandler.Sharable
public abstract class AbstractHandler<T extends Message> extends SimpleChannelInboundHandler<T> {

    @Autowired
    private NettyServer nettyServer;

    @Override
    public final void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        if (check()) {
            if (nettyServer.online.get(((SocketChannel) ctx.channel())).isLogin()) {
                read(ctx, msg);
            } else {
                ctx.writeAndFlush(new StringResponse("not login", false));
            }
        } else {
            read(ctx, msg);
        }
    }

    public abstract void read(ChannelHandlerContext ctx, T msg) throws Exception;

    /**
     * @return check is login
     */
    public boolean check() {
        return true;
    }
}
