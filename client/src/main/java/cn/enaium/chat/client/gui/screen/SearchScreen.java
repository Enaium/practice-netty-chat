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
import cn.enaium.chat.client.util.HttpUtil;
import cn.enaium.chat.client.util.OptionUtil;
import cn.enaium.chat.common.message.request.AddFriendRequestMessage;
import cn.enaium.chat.common.message.request.AddGroupRequestMessage;
import cn.enaium.chat.common.vo.InfoVO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.http.HttpResponse;

/**
 * @author Enaium
 */
public class SearchScreen extends DoneScreen {
    public SearchScreen(JPanel parent) {
        super(parent);


        var userInfoVODefaultListModel = new DefaultListModel<InfoVO>();
        var infoJList = new InfoVOJList<>(userInfoVODefaultListModel);
        add(new JScrollPane(infoJList), BorderLayout.CENTER);

        var searchPanel = new JPanel(new BorderLayout());
        var jTextField = new JTextField();
        searchPanel.add(jTextField, BorderLayout.CENTER);
        var buttonGroup = new ButtonGroup();
        var friend = new JRadioButton("Friend", true);
        buttonGroup.add(friend);
        var group = new JRadioButton("Group");
        buttonGroup.add(group);
        searchPanel.add(new JPanel() {
            {
                add(friend);
                add(group);
            }
        }, BorderLayout.NORTH);

        searchPanel.add(new JButton("Search") {
            {
                addActionListener(e -> {
                    if (!jTextField.getText().isEmpty()) {
                        System.out.println(friend.isSelected());
                        var body = HttpUtil.send(HttpUtil.get(String.format("/%s/query/%s", friend.isSelected() ? "user" : "group", jTextField.getText())), HttpResponse.BodyHandlers.ofString()).body();
                        userInfoVODefaultListModel.removeAllElements();
                        new Gson().fromJson(body, JsonArray.class).forEach(it -> userInfoVODefaultListModel.addElement(new Gson().fromJson(it.getAsJsonObject().toString(), InfoVO.class)));
                    } else {
                        OptionUtil.warning("empty");
                    }
                });
            }
        }, BorderLayout.EAST);

        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem jMenuItem = new JMenuItem("Add");
        jMenuItem.addActionListener(e -> {
            var type = infoJList.getSelectedValue().getType();
            if (type == InfoVO.Type.USER) {
                Chat.getInstance().network.send(new AddFriendRequestMessage(infoJList.getSelectedValue().getId()));
            } else if (type == InfoVO.Type.GROUP) {
                Chat.getInstance().network.send(new AddGroupRequestMessage(infoJList.getSelectedValue().getId()));
            }
        });
        jPopupMenu.add(jMenuItem);

        infoJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (infoJList.getSelectedValue() != null) {
                        jPopupMenu.show(SearchScreen.this, e.getX(), e.getY());
                    }
                }
            }
        });

        add(searchPanel, BorderLayout.NORTH);
    }
}
