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

import cn.enaium.chat.common.message.request.RegisterRequestMessage;
import cn.enaium.chat.common.message.response.RegisterResponseMessage;
import cn.enaium.chat.server.netty.annotation.Handler;
import cn.enaium.chat.server.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Enaium
 */
@Handler
public class RegisterHandler extends AbstractHandler<RegisterRequestMessage> {

    @Autowired
    private UserService userService;

    @Override
    public void read(ChannelHandlerContext ctx, RegisterRequestMessage msg) throws Exception {
        RegisterResponseMessage registerResponseMessage;

        if (msg.getUsername().replace(" ", "").isEmpty() || msg.getPassword().replace(" ", "").isEmpty()) {
            registerResponseMessage = new RegisterResponseMessage("empty username or password", false);
        } else if (userService.hasUsername(msg.getUsername())) {
            registerResponseMessage = new RegisterResponseMessage("username already exists", false);
        } else {
            userService.register(msg.getUsername(), msg.getPassword(), msg.getNickname());
            registerResponseMessage = new RegisterResponseMessage("success", true);
        }
        ctx.channel().writeAndFlush(registerResponseMessage);
    }

    @Override
    public boolean check() {
        return false;
    }
}
