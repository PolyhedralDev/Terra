package com.dfsek.terra.profiler;

import com.dfsek.terra.profiler.exception.MalformedStackException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProfilerImpl implements Profiler {
    private static final ThreadLocal<Stack<Frame>> THREAD_STACK = ThreadLocal.withInitial(Stack::new);
    private static final ThreadLocal<Map<String, Timings>> TIMINGS = ThreadLocal.withInitial(HashMap::new);
    private final List<Map<String, Timings>> accessibleThreadMaps = new ArrayList<>();
    private volatile boolean running = false;


    protected ProfilerImpl() {

    }

    @Override
    public void push(String frame) {
        if(running) THREAD_STACK.get().push(new Frame(frame));
    }

    @Override
    public void pop(String frame) {
        if(running) {
            long time = System.nanoTime();
            Stack<Frame> stack = THREAD_STACK.get();

            Map<String, Timings> timingsMap = TIMINGS.get();

            if(timingsMap.size() == 0) {
                synchronized(accessibleThreadMaps) {
                    accessibleThreadMaps.add(timingsMap);
                }
            }

            Timings bottom = timingsMap.computeIfAbsent(stack.get(0).getId(), id -> new Timings());

            for(int i = 1; i < stack.size(); i++) {
                bottom = bottom.getSubItem(stack.get(i).getId());
            }

            Frame top = stack.pop();
            if(!top.getId().equals(frame)) throw new MalformedStackException("Expected " + frame + ", found " + top);

            bottom.addTime(time - top.getStart());
        }
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
        accessibleThreadMaps.forEach(map::putAll);
        return map;
    }
}
