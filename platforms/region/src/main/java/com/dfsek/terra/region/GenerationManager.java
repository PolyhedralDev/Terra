package com.dfsek.terra.region;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.platform.DirectWorld;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GenerationManager {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(GenerationManager.class);
    private final ExecutorService executor;
    private final AtomicLong generatedChunks;
    private final DirectWorld world;
    private final AtomicLong time;

    public GenerationManager(DirectWorld world) {
        this.world = world;
        executor = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() * 1.5));
        generatedChunks = new AtomicLong();
        time = new AtomicLong(System.nanoTime());
    }

    public void registerGenerationTask(GenerationTask task, int x, int z) {
        this.executor.submit(new GenerationTaskWrapper(this, task, world, x, z));
    }

    public void submitCompletedChunk(Chunk chunk) {
        long count = generatedChunks.incrementAndGet();

        if(count % 200 == 0) {
            long n = System.nanoTime();

            logger.info("Generated {} chunks. {} cps.", count, 200 / ((double) (n - time.get()) / 1000000) * 1000);

            time.set(System.nanoTime());
        }
        // do nothing with generated chunk for now.
    }

    public void awaitTermination() throws InterruptedException {
        executor.shutdown();

        //noinspection ResultOfMethodCallIgnored
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public long getGeneratedChunks() {
        return generatedChunks.get();
    }

//    public GenerationManager() {
//
//    }
}
