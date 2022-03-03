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
import cn.enaium.chat.common.message.request.CreateGroupRequestMessage;

import javax.swing.*;
import java.awt.*;

/**
 * @author Enaium
 */
public class CreateGroupScreen extends DoneScreen {
    public CreateGroupScreen(JPanel parent) {
        super(parent);
        var nameTextField = new JTextField();
        var jPanel = new JPanel(new GridLayout(2, 0));
        jPanel.add(new JPanel() {
            {
                add(new JLabel("Group"));
                add(nameTextField);
            }
        });
        jPanel.add(new JPanel() {
            {
                add(new JButton("Create") {
                    {
                        addActionListener(e -> {
                            Chat.getInstance().network.send(new CreateGroupRequestMessage(nameTextField.getText()));
                        });
                    }
                });
            }
        });
        add(jPanel);
    }
}
