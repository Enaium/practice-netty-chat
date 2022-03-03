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

package cn.enaium.chat.client.gui.screen;

import cn.enaium.chat.client.Chat;
import cn.enaium.chat.client.gui.component.InfoVOJList;
import cn.enaium.chat.client.util.ChatUtil;
import cn.enaium.chat.client.util.HttpUtil;
import cn.enaium.chat.client.util.OptionUtil;
import cn.enaium.chat.common.message.request.ChatSendRequestMessage;
import cn.enaium.chat.common.message.request.GroupSendRequestMessage;
import cn.enaium.chat.common.vo.InfoVO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * @author Enaium
 */
public class ChatScreen extends JPanel {
    public ChatScreen() {
        setLayout(new BorderLayout());
        var friendListPanel = new JPanel(new BorderLayout());

        DefaultListModel<InfoVO> infoVODefaultListModel = new DefaultListModel<>();
        InfoVOJList<InfoVO> infoList = new InfoVOJList<>(infoVODefaultListModel);

        infoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (infoList.getSelectedValue() != null) {
                    Chat.getInstance().setTitle(String.format("%s(%s)", (infoList.getSelectedValue().getType() == InfoVO.Type.GROUP ? "Group:" : "") + infoList.getSelectedValue().getName(), infoList.getSelectedValue().getId()));
                }
                super.mouseClicked(e);
            }
        });

        friendListPanel.add(infoList, BorderLayout.CENTER);

        class Refresh {
            public void action() {
                infoVODefaultListModel.removeAllElements();
                var userBody = HttpUtil.send(HttpUtil.get(String.format("/user/getFriend/%s", Chat.getInstance().id)), HttpResponse.BodyHandlers.ofString()).body();
                new Gson().fromJson(userBody, JsonArray.class).forEach(it -> infoVODefaultListModel.addElement(new Gson().fromJson(it.getAsJsonObject().toString(), InfoVO.class)));
                var groupBody = HttpUtil.send(HttpUtil.get(String.format("/user/getGroup/%s", Chat.getInstance().id)), HttpResponse.BodyHandlers.ofString()).body();
                new Gson().fromJson(groupBody, JsonArray.class).forEach(it -> infoVODefaultListModel.addElement(new Gson().fromJson(it.getAsJsonObject().toString(), InfoVO.class)));

            }
        }

        friendListPanel.add(new JButton("Refresh") {
            {
                setHorizontalAlignment(SwingConstants.LEFT);
                addActionListener(e -> {
                    new Refresh().action();
                });
            }
        }, BorderLayout.NORTH);

        var addPanel = new JPanel(new GridLayout(2, 0));
        var add = new JButton("Add");
        add.addActionListener(e -> {
            Chat.getInstance().setScreen(new SearchScreen(this));
        });
        add.setHorizontalAlignment(SwingConstants.LEFT);
        addPanel.add(add);

        var create = new JButton("Create");
        create.addActionListener(e -> {
            Chat.getInstance().setScreen(new CreateGroupScreen(this));
        });
        create.setHorizontalAlignment(SwingConstants.LEFT);
        addPanel.add(create);
        friendListPanel.add(addPanel, BorderLayout.SOUTH);


        var friendSplit = new JSplitPane();

        friendSplit.setDividerLocation(100);
        friendSplit.setLeftComponent(new JScrollPane(friendListPanel));
        var chatPanel = new JPanel(new BorderLayout());

        var chatSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        chatSplit.setDividerLocation(200);
        var view = new JTextArea();
        view.setEditable(false);
        var viewScroll = new JScrollPane(view);
        chatSplit.setLeftComponent(viewScroll);//TOP

        var chatBottom = new JPanel(new BorderLayout());
        var sendMessage = new JTextArea();
        chatBottom.add(new JScrollPane(sendMessage), BorderLayout.CENTER);
        var send = new JButton("Send");

        send.addActionListener(e -> {
            if (infoList.getSelectedValue() != null) {

                if (infoList.getSelectedValue().getType() == InfoVO.Type.USER) {
                    var body = HttpUtil.send(HttpUtil.get(String.format("/user/isOnline/%s", infoList.getSelectedValue().getId())), HttpResponse.BodyHandlers.ofString()).body();
                    if (body.equals("false")) {
                        OptionUtil.info("user is offline");
                        return;
                    }

                    var message = new ChatSendRequestMessage(-1, infoList.getSelectedValue().getId(), sendMessage.getText());
                    Chat.getInstance().network.send(message);
                    try {
                        ChatUtil.record(infoList.getSelectedValue().getId(), message.getMsg(), "Send");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else if (infoList.getSelectedValue().getType() == InfoVO.Type.GROUP) {
                    var message = new GroupSendRequestMessage(-1, infoList.getSelectedValue().getId(), sendMessage.getText());
                    Chat.getInstance().network.send(message);
                    try {
                        ChatUtil.recordGroup(infoList.getSelectedValue().getId(), Chat.getInstance().id, message.getMsg(), "Send");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                var verticalScrollBar = viewScroll.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                sendMessage.setText("");
            }
        });

        chatBottom.add(send, BorderLayout.SOUTH);
        chatSplit.setRightComponent(chatBottom);

        chatPanel.add(chatSplit, BorderLayout.CENTER);

        friendSplit.setRightComponent(chatPanel);

        add(friendSplit, BorderLayout.CENTER);

        new Thread(() -> {
            while (true) {
                if (infoList.getSelectedValue() != null) {
                    try {
                        view.setText(ChatUtil.get(Chat.getInstance().id, infoList.getSelectedValue().getId(), infoList.getSelectedValue().getType() == InfoVO.Type.GROUP));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        new Refresh().action();
    }
}
