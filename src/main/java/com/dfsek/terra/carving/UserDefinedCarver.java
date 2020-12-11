package com.dfsek.terra.carving;

import com.dfsek.terra.api.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.gaea.world.carving.Carver;
import com.dfsek.terra.api.gaea.world.carving.Worm;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.math.RandomFunction;
import net.jafama.FastMath;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;

public class UserDefinedCarver extends Carver {
    private final double[] start; // 0, 1, 2 = x, y, z.
    private final double[] mutate; // 0, 1, 2 = x, y, z. 3 = radius.
    private final Range length;
    private final long hash;
    private final int topCut;
    private final int bottomCut;
    private final CarverTemplate config;
    private final Expression xRad;
    private final Expression yRad;
    private final Expression zRad;
    private final Variable lengthVar;
    private final Variable position;
    private final Variable seedVar;
    private final Map<World, CarverCache> cacheMap = new HashMap<>();
    private double step = 2;
    private Range recalc = new Range(8, 10);
    private double recalcMagnitude = 3;
    private final TerraBukkitPlugin main;

    public UserDefinedCarver(Range height, Range length, double[] start, double[] mutate, List<String> radii, Scope parent, long hash, int topCut, int bottomCut, CarverTemplate config, TerraBukkitPlugin main) throws ParseException {
        super(height.getMin(), height.getMax());
        this.length = length;
        this.start = start;
        this.mutate = mutate;
        this.hash = hash;
        this.topCut = topCut;
        this.bottomCut = bottomCut;
        this.config = config;
        this.main = main;

        Parser p = new Parser();

        p.registerFunction("rand", new RandomFunction());

        Scope s = new Scope().withParent(parent);

        lengthVar = s.create("length");
        position = s.create("position");
        seedVar = s.create("seed");


        xRad = p.parse(radii.get(0), s);
        yRad = p.parse(radii.get(1), s);
        zRad = p.parse(radii.get(2), s);

    }

    @Override
    public Worm getWorm(long l, Vector3 vector) {
        Random r = new FastRandom(l + hash);
        return new UserDefinedWorm(length.get(r) / 2, r, vector, topCut, bottomCut);
    }

    protected Variable getSeedVar() {
        return seedVar;
    }

    protected Variable getLengthVar() {
        return lengthVar;
    }

    protected Variable getPosition() {
        return position;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setRecalc(Range recalc) {
        this.recalc = recalc;
    }

    @Override
    public void carve(int chunkX, int chunkZ, World w, BiConsumer<Vector3, CarvingType> consumer) {
        CarverCache cache = cacheMap.computeIfAbsent(w, world -> new CarverCache(world, main));
        int carvingRadius = getCarvingRadius();
        for(int x = chunkX - carvingRadius; x <= chunkX + carvingRadius; x++) {
            for(int z = chunkZ - carvingRadius; z <= chunkZ + carvingRadius; z++) {
                cache.getPoints(x, z, this).forEach(point -> {
                    Vector3 origin = point.getOrigin();
                    if(FastMath.floorDiv(origin.getBlockX(), 16) != chunkX && FastMath.floorDiv(origin.getBlockZ(), 16) != chunkZ) // We only want to carve this chunk.
                        return;
                    point.carve(chunkX, chunkZ, consumer);
                });
            }
        }
    }

    public void setRecalcMagnitude(double recalcMagnitude) {
        this.recalcMagnitude = recalcMagnitude;
    }

    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random random) {
        BiomeTemplate conf = ((UserDefinedBiome) main.getWorld(w).getGrid().getBiome((chunkX << 4) + 8, (chunkZ << 4) + 8, GenerationPhase.POPULATE)).getConfig();
        if(conf.getCarvers().get(this) != null) {
            return new FastRandom(random.nextLong() + hash).nextInt(100) < conf.getCarvers().get(this);
        }
        return false;
    }

    public CarverTemplate getConfig() {
        return config;
    }

    private class UserDefinedWorm extends Worm {
        private final Vector3 direction;
        private int steps;
        private int nextDirection = 0;
        private double[] currentRotation = new double[3];

        public UserDefinedWorm(int length, Random r, Vector3 origin, int topCut, int bottomCut) {
            super(length, r, origin);
            super.setTopCut(topCut);
            super.setBottomCut(bottomCut);
            direction = new Vector3((r.nextDouble() - 0.5D) * start[0], (r.nextDouble() - 0.5D) * start[1], (r.nextDouble() - 0.5D) * start[2]).normalize().multiply(step);
            position.setValue(0);
            lengthVar.setValue(length);
            setRadius(new int[] {(int) (xRad.evaluate()), (int) (yRad.evaluate()), (int) (zRad.evaluate())});
        }

        @Override
        public WormPoint getPoint() {
            return new WormPoint(getRunning().clone(), getRadius(), config.getCutTop(), config.getCutBottom());
        }

        @Override
        public synchronized void step() {
            if(steps == nextDirection) {
                direction.rotateAroundX(FastMath.toRadians((getRandom().nextGaussian()) * mutate[0] * recalcMagnitude));
                direction.rotateAroundY(FastMath.toRadians((getRandom().nextGaussian()) * mutate[1] * recalcMagnitude));
                direction.rotateAroundZ(FastMath.toRadians((getRandom().nextGaussian()) * mutate[2] * recalcMagnitude));
                currentRotation = new double[] {(getRandom().nextGaussian()) * mutate[0],
                        (getRandom().nextGaussian()) * mutate[1],
                        (getRandom().nextGaussian()) * mutate[2]};
                nextDirection += recalc.get(getRandom());
            }
            steps++;
            position.setValue(steps);
            setRadius(new int[] {(int) (xRad.evaluate()), (int) (yRad.evaluate()), (int) (zRad.evaluate())});
            direction.rotateAroundX(FastMath.toRadians(currentRotation[0] * mutate[0]));
            direction.rotateAroundY(FastMath.toRadians(currentRotation[1] * mutate[1]));
            direction.rotateAroundZ(FastMath.toRadians(currentRotation[2] * mutate[2]));
            getRunning().add(direction);
        }
    }
}
