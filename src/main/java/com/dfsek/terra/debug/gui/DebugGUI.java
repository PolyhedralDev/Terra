package com.dfsek.terra.debug.gui;

import com.dfsek.terra.api.bukkit.TerraBukkitPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugGUI extends Thread {

    private final BufferedImage img;
    private final TerraBukkitPlugin main;

    public DebugGUI(BufferedImage img, TerraBukkitPlugin main) {
        this.img = img;
        this.main = main;
    }

    @Override
    public void run() {
        DebugFrame frame = new DebugFrame(img, "Image2Map Debug GUI", main);
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
