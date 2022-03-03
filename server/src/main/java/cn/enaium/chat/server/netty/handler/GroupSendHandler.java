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

import cn.enaium.chat.common.message.request.GroupSendRequestMessage;
import cn.enaium.chat.common.message.response.GroupSendResponseMessage;
import cn.enaium.chat.server.netty.NettyServer;
import cn.enaium.chat.server.netty.annotation.Handler;
import cn.enaium.chat.server.service.GroupService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Enaium
 */
@Handler
public class GroupSendHandler extends AbstractHandler<GroupSendRequestMessage> {

    @Autowired
    private GroupService groupService;

    @Autowired
    private NettyServer nettyServer;

    @Override
    public void read(ChannelHandlerContext ctx, GroupSendRequestMessage msg) throws Exception {
        var id = nettyServer.getClient(((SocketChannel) ctx.channel())).getId();
        for (Long aLong : groupService.getMember(msg.getGroup())) {
            if (aLong != id) {
                nettyServer.getSocketChannel(aLong).writeAndFlush(new GroupSendRequestMessage(id, msg.getGroup(), msg.getMsg()));
            }
        }
        ctx.channel().writeAndFlush(new GroupSendResponseMessage("send success", true));
    }
}