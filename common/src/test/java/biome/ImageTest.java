package biome;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.ImageBiomeProvider;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.AbstractableTemplate;
import com.dfsek.terra.registry.OpenRegistry;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ImageTest {
    private static ImageBiomeProvider getProvider(long seed) throws ConfigException, IOException {
        System.out.println(seed);
        File pack = new File("/home/dfsek/Documents/Terra/platforms/bukkit/target/server/plugins/Terra/packs/default/");
        FolderLoader folderLoader = new FolderLoader(pack.toPath());

        AbstractConfigLoader loader = new AbstractConfigLoader();

        OpenRegistry<TerraBiome> biomeRegistry = new OpenRegistry<TerraBiome>() {
        };
        folderLoader.open("biomes", ".yml").then(inputStreams -> ConfigPack.buildAll((template, main) -> template, biomeRegistry, loader.loadConfigs(inputStreams, TestBiome::new), null));

        return new ImageBiomeProvider(biomeRegistry.entries(), ImageIO.read(ImageTest.class.getResourceAsStream("/map.jpg")), 1, ImageBiomeProvider.Align.CENTER);
    }

    @Test
    public static void main(String... args) throws ConfigException, IOException {
        JFrame testFrame = new JFrame("Biome Viewer");


        final BiomeProvider[] provider = {getProvider(2403)};


        int size = 1024;
        final BufferedImage[] image = {new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)};
        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                image[0].setRGB(x, z, provider[0].getBiome(x, z).getColor());
            }
        }

        JLabel img = new JLabel(new ImageIcon(image[0]));

        testFrame.add(img);
        testFrame.pack();
        img.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage newImage = deepCopy(image[0]);
                Graphics graphics = newImage.getGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, 512, 24);
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Monospace", Font.BOLD, 20));
                graphics.drawString(provider[0].getBiome(e.getX(), e.getY()).toString(), 0, 20);

                graphics.setColor(Color.WHITE);
                graphics.fillOval(e.getX() - 2, e.getY() - 2, 12, 12);
                graphics.setColor(Color.BLACK);
                graphics.fillOval(e.getX(), e.getY(), 8, 8);

                img.setIcon(new ImageIcon(newImage));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        testFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == 's') {
                    try {
                        provider[0] = getProvider(ThreadLocalRandom.current().nextLong());
                    } catch(ConfigException | IOException configException) {
                        configException.printStackTrace();
                    }
                    image[0] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                    for(int x = 0; x < size; x++) {
                        for(int z = 0; z < size; z++) {
                            image[0].setRGB(x, z, provider[0].getBiome(x, z).getColor());
                        }
                    }
                    img.setIcon(new ImageIcon(image[0]));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        testFrame.setResizable(false);
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testFrame.setVisible(true);

    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static final class BiomeProviderTemplate implements ConfigTemplate {
        @Value("biome-pipeline")
        BiomeProvider.BiomeProviderBuilder biomeProviderBuilder;

        public BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder() {
            return biomeProviderBuilder;
        }
    }

    private static final class TestBiome extends AbstractableTemplate implements TerraBiome, ValidatedConfigTemplate {

        @Value("color")
        @Default
        @Abstractable
        private int color;

        @Value("tags")
        @Abstractable
        @Default
        private Set<String> tags = new HashSet<>();

        @Value("id")
        private String id;

        @Override
        public ProbabilityCollection<Biome> getVanillaBiomes() {
            return null;
        }

        @Override
        public Generator getGenerator(World w) {
            return null;
        }

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public Set<String> getTags() {
            return tags;
        }

        @Override
        public boolean validate() {
            color += (255 << 24); // Alpha adjustment
            tags.add("BIOME:" + id);
            return true;
        }

        @Override
        public String getID() {
            return id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
