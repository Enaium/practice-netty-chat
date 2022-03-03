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

import cn.enaium.chat.common.message.request.ChatSendRequestMessage;
import cn.enaium.chat.common.message.response.ChatSendResponseMessage;
import cn.enaium.chat.server.netty.Client;
import cn.enaium.chat.server.netty.NettyServer;
import cn.enaium.chat.server.netty.annotation.Handler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author Enaium
 */
@Handler
public class ChatSendHandler extends AbstractHandler<ChatSendRequestMessage> {

    @Autowired
    private NettyServer nettyServer;

    @Override
    public void read(ChannelHandlerContext ctx, ChatSendRequestMessage msg) {
        ChatSendResponseMessage responseMessage = new ChatSendResponseMessage("user is offline", false);

        for (Map.Entry<SocketChannel, Client> socketChannelClientEntry : nettyServer.online.entrySet()) {
            if (socketChannelClientEntry.getValue().getId() == msg.getTo()) {
                socketChannelClientEntry.getKey().writeAndFlush(new ChatSendRequestMessage(nettyServer.getClient(((SocketChannel) ctx.channel())).getId(), msg.getTo(), msg.getMsg()));
                responseMessage = new ChatSendResponseMessage("send success", true);
            }
        }

        ctx.writeAndFlush(responseMessage);
    }
}
