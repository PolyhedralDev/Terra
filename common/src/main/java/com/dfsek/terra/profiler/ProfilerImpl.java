package com.dfsek.terra.profiler;

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


    public ProfilerImpl() {

    }

    @Override
    public void push(String frame) {
        if(running) {
            Stack<Frame> stack = THREAD_STACK.get();
            stack.push(new Frame(stack.size() == 0 ? frame : stack.peek().getId() + "." + frame));
        }
    }

    @Override
    public void pop(String frame) {
        if(running) {
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
        accessibleThreadMaps.forEach(smap -> {
            smap.forEach((key, list) -> {
                String[] keys = key.split("\\.");
                Timings timings = map.computeIfAbsent(keys[0], id -> new Timings());
                for(int i = 1; i < keys.length; i++) {
                    timings = timings.getSubItem(keys[i]);
                }
                list.forEach(timings::addTime);
            });
        });
        return map;
    }
}
