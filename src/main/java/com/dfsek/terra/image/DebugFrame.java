package com.dfsek.terra.image;

import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.polydev.gaea.generation.GenerationPhase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class DebugFrame extends JFrame implements ActionListener {
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
            if(! (p.getWorld().getGenerator() instanceof TerraChunkGenerator)) break;
            int xp = (int) (((double) Math.floorMod(p.getLocation().getBlockX(), x) / x) * getWidth());
            int zp = (int) (((double) Math.floorMod(p.getLocation().getBlockZ(), z) / z) * getHeight());
            if(WorldConfig.fromWorld(p.getWorld()).imageLoader.getAlign().equals(ImageLoader.Align.CENTER)) {
                xp = (int) (((double) Math.floorMod(p.getLocation().getBlockX() - (img.getWidth() / 2), x) / x) * getWidth());
                zp = (int) (((double) Math.floorMod(p.getLocation().getBlockZ() - (img.getHeight() / 2), z) / z) * getHeight());
            }
            String str = BiomeConfig.fromBiome((UserDefinedBiome) TerraBiomeGrid.fromWorld(p.getWorld()).getBiome(p.getLocation(), GenerationPhase.POPULATE)).getID();
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
