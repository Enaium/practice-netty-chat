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

package cn.enaium.chat.client.util;

import cn.enaium.chat.client.Chat;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * @author Enaium
 */
public class ChatUtil {
    public static void record(long from, String msg, String type) throws IOException {
        var dir = new File(System.getProperty("user.dir"), String.format("%s/%s/%s", Chat.getInstance().id, "chat", "user"));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        var file = new File(dir, from + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.writeString(file.toPath(), String.format("[%s][%s][%s]:%s\n", new Date(), getNickname(from), type, msg), StandardOpenOption.APPEND);
    }

    public static void recordGroup(long group, long from, String msg, String type) throws IOException {
        var dir = new File(System.getProperty("user.dir"), String.format("%s/%s/%s", Chat.getInstance().id, "chat", "group"));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        var file = new File(dir, group + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.writeString(file.toPath(), String.format("[%s][%s][%s]:%s\n", new Date(), getNickname(from), type, msg), StandardOpenOption.APPEND);
    }

    public static String get(long id, long infoId, boolean group) throws IOException {
        var file = new File(System.getProperty("user.dir"), String.format("%s/chat/%s/%s.txt", id, group ? "group" : "user", infoId));
        if (file.exists()) {
            return Files.readString(file.toPath());
        } else {
            return null;
        }
    }

    public static String getNickname(long id) {
        var send = HttpUtil.send(HttpUtil.get(String.format("/user/getNickname/%s", id)), HttpResponse.BodyHandlers.ofString());
        return send.body();
    }
}
