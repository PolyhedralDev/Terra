package profiler;

import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;

public class ProfilerTest {
    private static final Profiler PROFILER = new ProfilerImpl();
    //@Test
    public static void main(String... a) throws InterruptedException {
        PROFILER.start();
        for(int i = 0; i < 1000; i++) {
            doThing();
        }

        for(int i = 0; i < 100; i++) {
            doThirdOtherThing();
        }

        for(int i = 0; i < 100; i++) {
            doOtherThing();
        }
        PROFILER.stop();
        PROFILER.getTimings().forEach((id, timings) -> {
            System.out.println(id + ": " + timings.toString());
        });
    }

    private static void doThing() throws InterruptedException {
        PROFILER.push("thing");
        Thread.sleep(1);
        doOtherThing();
        thing4();
        PROFILER.pop("thing");
    }

    private static void doOtherThing() throws InterruptedException {
        PROFILER.push("thing2");
        Thread.sleep(2);
        doThirdOtherThing();
        thing4();
        PROFILER.pop("thing2");
    }

    private static void doThirdOtherThing() throws InterruptedException {
        PROFILER.push("thing3");
        Thread.sleep(2);
        PROFILER.pop("thing3");
    }

    private static void thing4() throws InterruptedException {
        PROFILER.push("thing4");
        Thread.sleep(2);
        PROFILER.pop("thing4");
    }
}
