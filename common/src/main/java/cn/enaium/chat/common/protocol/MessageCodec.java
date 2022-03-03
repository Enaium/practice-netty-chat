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

package cn.enaium.chat.common.protocol;

import cn.enaium.chat.common.message.Message;
import cn.enaium.chat.common.message.MessageType;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Enaium
 */
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    private final int serialize = 1;//0JDK,1Gson,2Hessian

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws IOException {
        var buffer = ctx.alloc().buffer();
        buffer.writeInt(0xFAFBFCFD);//magic number
        buffer.writeByte(1);//version
        buffer.writeByte(msg.getMessageType().getId());//content type
        buffer.writeByte(serialize);//serialize type
        buffer.writeByte(0xFF);
        var bytes = new byte[]{};
        switch (serialize) {
            case 1 -> bytes = new Gson().toJson(msg).getBytes(StandardCharsets.UTF_8);
            case 2 -> {
                var byteArrayOutputStream = new ByteArrayOutputStream();
                var ho = new HessianOutput(byteArrayOutputStream);
                ho.writeObject(msg);
                bytes = byteArrayOutputStream.toByteArray();
            }
            default -> {
                var byteArrayOutputStream = new ByteArrayOutputStream();
                var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(msg);
                bytes = byteArrayOutputStream.toByteArray();
            }
        }

        buffer.writeInt(bytes.length);//content of length
        buffer.writeBytes(bytes);//content
        out.add(buffer);
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws IOException, ClassNotFoundException {
        var magic = msg.readInt();
        var version = msg.readByte();
        var contentType = msg.readByte();
        Class<? extends Message> klass = MessageType.getById(contentType).getKlass();
        var serializeType = msg.readByte();
        msg.readByte();//0xFF
        var contentLength = msg.readInt();
        var bytes = new byte[contentLength];
        msg.readBytes(bytes);

        Message message;

        switch (serializeType) {
            case 1 -> message = new Gson().fromJson(new String(bytes, StandardCharsets.UTF_8), klass);
            case 2 -> {
                message = (Message) new HessianInput(new ByteArrayInputStream(bytes)).readObject();
            }
            default -> {
                var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                message = (Message) objectInputStream.readObject();
            }
        }
        out.add(message);
    }
}
