package com.dfsek.terra.profiler;

import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.profiler.exception.MalformedStackException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProfilerImpl implements Profiler {
    private static final ThreadLocal<Stack<Frame>> THREAD_STACK = ThreadLocal.withInitial(Stack::new);
    private static final ThreadLocal<Map<String, List<Long>>> TIMINGS = ThreadLocal.withInitial(HashMap::new);
    private final List<Map<String, List<Long>>> accessibleThreadMaps = new ArrayList<>();
    private volatile boolean running = false;
    private static boolean instantiated = false;

    private static final ThreadLocal<Boolean> SAFE = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<MutableInteger> STACK_SIZE = ThreadLocal.withInitial(() -> new MutableInteger(0));

    public ProfilerImpl() {
        if(instantiated) throw new IllegalStateException("Only one instance of Profiler may exist!");
        instantiated = true;
    }

    @Override
    public void push(String frame) {
        STACK_SIZE.get().increment();
        if(running && SAFE.get()) {
            Stack<Frame> stack = THREAD_STACK.get();
            stack.push(new Frame(stack.isEmpty() ? frame : stack.peek().getId() + "." + frame));
        } else SAFE.set(false);
    }

    @Override
    public void pop(String frame) {
        MutableInteger size = STACK_SIZE.get();
        size.decrement();
        if(running && SAFE.get()) {
            long time = System.nanoTime();
            Stack<Frame> stack = THREAD_STACK.get();

            Map<String, List<Long>> timingsMap = TIMINGS.get();

            if(timingsMap.size() == 0) {
                synchronized(accessibleThreadMaps) {
                    accessibleThreadMaps.add(timingsMap);
                }
            }

            Frame top = stack.pop();
            if((stack.size() != 0 && !top.getId().endsWith("." + frame)) || (stack.size() == 0 && !top.getId().equals(frame)))
                throw new MalformedStackException("Expected " + frame + ", found " + top);

            List<Long> timings = timingsMap.computeIfAbsent(top.getId(), id -> new ArrayList<>());

            timings.add(time - top.getStart());
        }
        if(size.get() == 0) SAFE.set(true);
    }

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public Map<String, Timings> getTimings() {
        Map<String, Timings> map = new HashMap<>();
        accessibleThreadMaps.forEach(smap -> smap.forEach((key, list) -> {
            String[] keys = key.split("\\.");
            Timings timings = map.computeIfAbsent(keys[0], id -> new Timings());
            for(int i = 1; i < keys.length; i++) {
                timings = timings.getSubItem(keys[i]);
            }
            list.forEach(timings::addTime);
        }));
        return map;
    }

    @Override
    public void reset() {
        accessibleThreadMaps.forEach(Map::clear);
    }
}
