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
import cn.enaium.chat.client.gui.screen.ChatScreen;
import cn.enaium.chat.client.gui.screen.TitleScreen;
import cn.enaium.chat.client.util.ChatUtil;
import cn.enaium.chat.client.util.OptionUtil;
import cn.enaium.chat.common.message.Message;
import cn.enaium.chat.common.message.request.ChatSendRequestMessage;
import cn.enaium.chat.common.message.request.GroupSendRequestMessage;
import cn.enaium.chat.common.message.response.AbstractResponse;
import cn.enaium.chat.common.message.response.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketException;

/**
 * @author Enaium
 */
public class NetworkHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (msg instanceof AbstractResponse<?>) {
            if (msg instanceof LoginResponseMessage) {
                if (((LoginResponseMessage) msg).isSuccess()) {
                    Chat.getInstance().id = Long.parseLong(((LoginResponseMessage) msg).getResult());
                    Chat.getInstance().setScreen(new ChatScreen());
                } else {
                    OptionUtil.warning(((LoginResponseMessage) msg).getResult());
                }
            } else {
                if (((AbstractResponse<?>) msg).isSuccess()) {
                    OptionUtil.info((String) ((AbstractResponse<?>) msg).getResult());
                } else {
                    OptionUtil.warning((String) ((AbstractResponse<?>) msg).getResult());
                }
            }
        } else if (msg instanceof ChatSendRequestMessage) {
            ChatUtil.record(((ChatSendRequestMessage) msg).getFrom(), ((ChatSendRequestMessage) msg).getMsg(), "Receive");
        } else if (msg instanceof GroupSendRequestMessage) {
            ChatUtil.recordGroup(((GroupSendRequestMessage) msg).getGroup(), ((GroupSendRequestMessage) msg).getFrom(), ((GroupSendRequestMessage) msg).getMsg(), "Receive");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException) {
            OptionUtil.error(cause);
            Chat.getInstance().setScreen(new TitleScreen());
        }
        super.exceptionCaught(ctx, cause);
    }
}
