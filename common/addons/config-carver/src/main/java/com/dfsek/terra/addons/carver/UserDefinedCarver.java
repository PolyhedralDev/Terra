package com.dfsek.terra.addons.carver;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import net.jafama.FastMath;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import com.dfsek.terra.addons.carver.carving.Carver;
import com.dfsek.terra.addons.carver.carving.Worm;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;


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
    
    private final Map<Long, CarverCache> cacheMap = new ConcurrentHashMap<>();
    private final Platform main;
    private double step = 2;
    private Range recalc = new ConstantRange(8, 10);
    private double recalcMagnitude = 3;
    
    public UserDefinedCarver(Range height, Range length, double[] start, double[] mutate, List<String> radii, Scope parent, long hash,
                             int topCut, int bottomCut, CarverTemplate config, Platform main) throws ParseException {
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
        
        Scope s = new Scope().withParent(parent);
        
        
        s.addInvocationVariable("x");
        s.addInvocationVariable("y");
        s.addInvocationVariable("z");
        
        s.addInvocationVariable("length");
        s.addInvocationVariable("position");
        s.addInvocationVariable("seed");
        
        
        xRad = p.parse(radii.get(0), s);
        yRad = p.parse(radii.get(1), s);
        zRad = p.parse(radii.get(2), s);
        
    }
    
    @Override
    public void carve(int chunkX, int chunkZ, World w, BiConsumer<Vector3, CarvingType> consumer) {
        synchronized(cacheMap) {
            CarverCache cache = cacheMap.computeIfAbsent(w.getSeed(), world -> new CarverCache(w, main, this));
            int carvingRadius = getCarvingRadius();
            for(int x = chunkX - carvingRadius; x <= chunkX + carvingRadius; x++) {
                for(int z = chunkZ - carvingRadius; z <= chunkZ + carvingRadius; z++) {
                    cache.getPoints(x, z).forEach(point -> {
                        Vector3 origin = point.getOrigin();
                        if(FastMath.floorDiv(origin.getBlockX(), 16) != chunkX && FastMath.floorDiv(origin.getBlockZ(), 16) !=
                                                                                  chunkZ) // We only want to carve this chunk.
                            return;
                        point.carve(chunkX, chunkZ, consumer, w);
                    });
                }
            }
        }
    }
    
    @Override
    public Worm getWorm(long l, Vector3 vector) {
        Random r = new Random(l + hash);
        return new UserDefinedWorm(length.get(r) / 2, r, vector, topCut, bottomCut, l);
    }
    
    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random random) {
        /*BiomeTemplate conf = ((UserDefinedBiome) main.getWorld(w).getBiomeProvider().getBiome((chunkX << 4) + 8, (chunkZ << 4) + 8))
        .getConfig();
        if(conf.getCarvers().get(this) != null) {
            return new Random(random.nextLong() + hash).nextInt(100) < conf.getCarvers().get(this);
        }*/
        return false;
    }
    
    public void setRecalc(Range recalc) {
        this.recalc = recalc;
    }
    
    public void setRecalcMagnitude(double recalcMagnitude) {
        this.recalcMagnitude = recalcMagnitude;
    }
    
    public void setStep(double step) {
        this.step = step;
    }
    
    public CarverTemplate getConfig() {
        return config;
    }
    
    private class UserDefinedWorm extends Worm {
        private final Vector3 direction;
        private final Vector3 origin;
        private final long seed;
        private int steps;
        private int nextDirection = 0;
        private double[] currentRotation = new double[3];
        
        public UserDefinedWorm(int length, Random r, Vector3 origin, int topCut, int bottomCut, long seed) {
            super(length, r, origin);
            this.origin = origin;
            this.seed = seed;
            super.setTopCut(topCut);
            super.setBottomCut(bottomCut);
            direction = new Vector3((r.nextDouble() - 0.5D) * start[0], (r.nextDouble() - 0.5D) * start[1],
                                    (r.nextDouble() - 0.5D) * start[2]).normalize().multiply(step);
            double[] args = { origin.getX(), origin.getY(), origin.getZ(), length, 0, seed };
            setRadius(new int[]{ (int) (xRad.evaluate(args)), (int) (yRad.evaluate(args)), (int) (zRad.evaluate(args)) });
        }
        
        @Override
        public void step() {
            if(steps == nextDirection) {
                direction.rotateAroundX(FastMath.toRadians((getRandom().nextGaussian()) * mutate[0] * recalcMagnitude));
                direction.rotateAroundY(FastMath.toRadians((getRandom().nextGaussian()) * mutate[1] * recalcMagnitude));
                direction.rotateAroundZ(FastMath.toRadians((getRandom().nextGaussian()) * mutate[2] * recalcMagnitude));
                currentRotation = new double[]{
                        (getRandom().nextGaussian()) * mutate[0],
                        (getRandom().nextGaussian()) * mutate[1],
                        (getRandom().nextGaussian()) * mutate[2]
                };
                nextDirection += recalc.get(getRandom());
            }
            steps++;
            double[] args = { origin.getX(), origin.getY(), origin.getZ(), getLength(), steps, seed };
            setRadius(new int[]{ (int) (xRad.evaluate(args)), (int) (yRad.evaluate(args)), (int) (zRad.evaluate(args)) });
            direction.rotateAroundX(FastMath.toRadians(currentRotation[0] * mutate[0]));
            direction.rotateAroundY(FastMath.toRadians(currentRotation[1] * mutate[1]));
            direction.rotateAroundZ(FastMath.toRadians(currentRotation[2] * mutate[2]));
            getRunning().add(direction);
        }
        
        @Override
        public WormPoint getPoint() {
            return new WormPoint(getRunning().clone(), getRadius(), config.getCutTop(), config.getCutBottom());
        }
    }
}
