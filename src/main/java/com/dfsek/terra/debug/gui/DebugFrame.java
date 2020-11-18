package com.dfsek.terra.debug.gui;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.image.ImageLoader;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.polydev.gaea.generation.GenerationPhase;

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

    public DebugFrame(BufferedImage image, String s) {
        super(s);
        this.x = image.getWidth();
        this.z = image.getHeight();
        this.img = image;
        new Timer(500, this).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!(p.getWorld().getGenerator() instanceof TerraChunkGenerator)) break;
            int xp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockX() - (img.getWidth() / 2), x) / x) * getWidth());
            int zp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockZ() - (img.getHeight() / 2), z) / z) * getHeight());
            ImageLoader loader = TerraWorld.getWorld(p.getWorld()).getWorldConfig().imageLoader;
            if(loader != null && loader.getAlign().equals(ImageLoader.Align.NONE)) {
                xp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockX(), x) / x) * getWidth());
                zp = (int) (((double) FastMath.floorMod(p.getLocation().getBlockZ(), z) / z) * getHeight());
            }
            String str = TerraWorld.getWorld(p.getWorld()).getConfig().getBiome((UserDefinedBiome) TerraWorld.getWorld(p.getWorld()).getGrid().getBiome(p.getLocation(), GenerationPhase.POPULATE)).getID();
            g.setColor(new Color(255, 255, 255, 128));
            g.fillRect(xp + 13, zp - 13, (int) (8 + 8.25 * str.length()), 33);
            g.setColor(Color.BLACK);
            g.drawString(p.getName(), xp + 15, zp);
            g.drawString(str, xp + 15, zp + 15);
            g.fillOval(xp, zp, 10, 10);
            g.setColor(Color.RED);
            g.fillOval(xp + 3, zp + 3, 5, 5);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }
}
