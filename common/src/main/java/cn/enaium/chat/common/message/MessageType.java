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

package cn.enaium.chat.common.message;

import cn.enaium.chat.common.message.request.*;
import cn.enaium.chat.common.message.response.*;

/**
 * @author Enaium
 */
public enum MessageType {
    LOGIN_REQUEST(0, LoginRequestMessage.class),
    LOGIN_RESPONSE(1, LoginResponseMessage.class),
    REGISTER_REQUEST(2, RegisterRequestMessage.class),
    REGISTER_RESPONSE(3, RegisterResponseMessage.class),
    CHAT_SEND_REQUEST(4, ChatSendRequestMessage.class),
    CHAT_SEND_RESPONSE(5, ChatSendResponseMessage.class),
    CREATE_GROUP_REQUEST(6, CreateGroupRequestMessage.class),
    CREATE_GROUP_RESPONSE(7, CreateGroupResponseMessage.class),
    ADD_FRIEND_REQUEST(8, AddFriendRequestMessage.class),
    ADD_FRIEND_RESPONSE(9, AddFriendResponseMessage.class),
    ADD_GROUP_REQUEST(10, AddGroupRequestMessage.class),
    ADD_GROUP_RESPONSE(11, AddGroupResponseMessage.class),
    GROUP_SEND_REQUEST(12, GroupSendRequestMessage.class),
    GROUP_SEND_RESPONSE(13, GroupSendResponseMessage.class),
    STRING_RESPONSE(Byte.MAX_VALUE, StringResponse.class);
    
    private final int id;
    private final Class<? extends Message> klass;

    MessageType(int id, Class<? extends Message> klass) {
        this.id = id;
        this.klass = klass;
    }

    public int getId() {
        return id;
    }

    public Class<? extends Message> getKlass() {
        return klass;
    }

    public static MessageType getById(int id) {
        for (MessageType value : MessageType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        throw new NullPointerException(String.format("this id='%s' has no type", id));
    }
}
