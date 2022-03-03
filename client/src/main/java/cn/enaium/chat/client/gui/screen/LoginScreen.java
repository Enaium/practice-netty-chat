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
import cn.enaium.chat.client.util.OptionUtil;
import cn.enaium.chat.common.message.request.LoginRequestMessage;
import cn.enaium.chat.common.message.request.RegisterRequestMessage;

import javax.swing.*;
import java.awt.*;

/**
 * @author Enaium
 */
public class LoginScreen extends JPanel {
    public LoginScreen() {

        Chat.getInstance().network.connect(Chat.getInstance().network.create());

        setLayout(new GridLayout(4, 0));

        var title = new JPanel();
        title.add(new JLabel("Welcome to chat"));
        add(title);

        var username = new JPanel();
        username.add(new JLabel("Username"));
        var usernameTextField = new JTextField(12);
        username.add(usernameTextField);
        add(username);

        var password = new JPanel();
        password.add(new JLabel("Password"));
        var passwordTextField = new JPasswordField(12);
        password.add(passwordTextField);
        add(password);

        var control = new JPanel();


        var login = new JButton("Login");
        login.addActionListener(e -> Chat.getInstance().network.send(new LoginRequestMessage(usernameTextField.getText(), new String(passwordTextField.getPassword()))));

        var register = new JButton("Register");
        register.addActionListener(e -> {
            var s = JOptionPane.showInputDialog(null, "Nickname:");
            if (s != null) {
                if (!s.replace(" ", "").isEmpty()) {
                    Chat.getInstance().network.send(new RegisterRequestMessage(usernameTextField.getText(), new String(passwordTextField.getPassword()), s));
                } else {
                    OptionUtil.warning("Nickname is empty!");
                }
            }
        });

        control.add(login, BorderLayout.WEST);
        control.add(register, BorderLayout.CENTER);
        add(control);
    }
}
