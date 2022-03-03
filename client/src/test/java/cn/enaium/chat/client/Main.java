package cn.enaium.chat.client;

import cn.enaium.chat.client.gui.screen.CreateGroupScreen;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

/**
 * @author Enaium
 */
public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        JFrame jFrame = new JFrame("Test");
        jFrame.setSize(500, 500);
        jFrame.setLocationRelativeTo(jFrame.getOwner());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(new CreateGroupScreen(null));
        jFrame.setVisible(true);
    }
}
