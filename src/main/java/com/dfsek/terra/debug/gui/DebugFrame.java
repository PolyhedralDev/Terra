package com.dfsek.terra.debug.gui;

import com.dfsek.terra.api.implementations.bukkit.TerraBukkitPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class DebugFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 9133084939622854303L;
    private final int x;
    private final int z;
    private final BufferedImage img;
    private final TerraBukkitPlugin main;

    public DebugFrame(BufferedImage image, String s, TerraBukkitPlugin main) {
        super(s);
        this.x = image.getWidth();
        this.z = image.getHeight();
        this.img = image;
        this.main = main;
        new Timer(500, this).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        /*
        for(Player p : Bukkit.getOnlinePlayers()) {
            int xp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockX() - (img.getWidth() / 2), x) / x) * getWidth());
            int zp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockZ() - (img.getHeight() / 2), z) / z) * getHeight());
            ImageLoader loader = main.getWorld(p.getWorld()).getConfig().getTemplate().getImageLoader();
            if(loader != null && loader.getAlign().equals(ImageLoader.Align.NONE)) {
                xp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockX(), x) / x) * getWidth());
                zp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockZ(), z) / z) * getHeight());
            }
            String str = ((UserDefinedBiome) main.getWorld(p.getWorld()).getGrid().getBiome(p.getLocation(), GenerationPhase.POPULATE)).getID();
            g.setColor(new Color(255, 255, 255, 128));
            g.fillRect(xp + 13, zp - 13, (int) (8 + 8.25 * str.length()), 33);
            g.setColor(Color.BLACK);
            g.drawString(p.getName(), xp + 15, zp);
            g.drawString(str, xp + 15, zp + 15);
            g.fillOval(xp, zp, 10, 10);
            g.setColor(Color.RED);
            g.fillOval(xp + 3, zp + 3, 5, 5);
        }

         */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }
}
