package com.dfsek.terra.debug.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugGUI extends Thread {

    private final BufferedImage img;
    public DebugGUI(BufferedImage img) {
        this.img = img;
    }
    @Override
    public void run() {
        DebugFrame frame = new DebugFrame(img, "Image2Map Debug GUI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(1000, 1000, Image.SCALE_SMOOTH));
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        frame.getContentPane().add(jLabel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
