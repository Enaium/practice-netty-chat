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

package cn.enaium.chat.client;

import cn.enaium.chat.client.gui.MainGUI;
import cn.enaium.chat.client.gui.screen.TitleScreen;
import cn.enaium.chat.client.network.NetworkManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Enaium
 */
public class Chat {

    private static Chat instance;
    private final MainGUI mainGUI = new MainGUI();

    public NetworkManager network;

    public long id;

    public Chat() {
        instance = this;
        network = new NetworkManager();
    }

    public void run() {
        mainGUI.setVisible(true);
        setScreen(new TitleScreen());
    }

    public static void stop() {
        System.out.println("Stop!");
        if (instance != null) {
            try {
                instance.network.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setScreen(JPanel screen) {
        if (screen == null) {
            return;
        }
        var jPanel = mainGUI.getPanel();
        jPanel.removeAll();
        jPanel.repaint();
        jPanel.revalidate();
        jPanel.add(screen, BorderLayout.CENTER);
        jPanel.repaint();
        jPanel.revalidate();
    }

    public void setTitle(String title) {
        mainGUI.setTitle(title);
    }

    public static Chat getInstance() {
        return instance;
    }
}
