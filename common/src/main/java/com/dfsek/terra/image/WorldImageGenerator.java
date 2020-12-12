package com.dfsek.terra.image;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.biome.NormalizationUtil;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WorldImageGenerator {
    private final World w;
    private final BufferedImage draw;
    private final TerraPlugin main;

    public WorldImageGenerator(World w, int width, int height, TerraPlugin main) {
        draw = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.w = w;
        this.main = main;
    }

    public WorldImageGenerator drawWorld(int centerX, int centerZ) {
        TerraWorld tw = main.getWorld(w);
        TerraBiomeGrid tb = tw.getGrid();
        int imY = 0;
        for(int y = centerZ - (draw.getHeight() / 2); y < centerZ + (draw.getHeight() / 2); y++) {
            int imX = 0;
            for(int x = centerX - (draw.getWidth() / 2); x < centerX + (draw.getWidth() / 2); x++) {
                int zone = NormalizationUtil.normalize(tw.getZone().getRawNoise(x, y), 256, 4);
                double[] noise = tb.getGrid(x, y).getRawNoise(x, y);
                Color c = new Color(NormalizationUtil.normalize(noise[0], 256, 4), NormalizationUtil.normalize(noise[1], 256, 4), zone);
                draw.setRGB(imX, imY, c.getRGB());
                imX++;
            }
            imY++;
        }
        return this;
    }

    public BufferedImage getDraw() {
        return draw;
    }

    public void save(File file) {
        try {
            ImageIO.write(draw, "png", file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
