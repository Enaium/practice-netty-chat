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

package cn.enaium.chat.common.message.request;

import cn.enaium.chat.common.message.Message;
import cn.enaium.chat.common.message.MessageType;

/**
 * @author Enaium
 */
public class ChatSendRequestMessage extends Message {

    private final long from;
    private final long to;
    private final String msg;

    public ChatSendRequestMessage(long from, long to, String msg) {
        this.from = from;
        this.to = to;
        this.msg = msg;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ChatSendRequestMessage{" +
                "form=" + from +
                ", to=" + to +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT_SEND_REQUEST;
    }
}
