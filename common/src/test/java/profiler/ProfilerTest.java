package profiler;

import com.dfsek.terra.profiler.Profiler;

public class ProfilerTest {
    //@Test
    public static void main(String... a) throws InterruptedException {
        Profiler.INSTANCE.start();
        for(int i = 0; i < 100; i++) {
            doThing();
        }

        for(int i = 0; i < 100; i++) {
            doThirdOtherThing();
        }

        for(int i = 0; i < 100; i++) {
            doOtherThing();
        }
        Profiler.INSTANCE.stop();
        Profiler.INSTANCE.getTimings().forEach((id, timings) -> {
            System.out.println(id + ": " + timings.toString());
        });
    }

    private static void doThing() throws InterruptedException {
        Profiler.INSTANCE.push("thing");
        Thread.sleep(1);
        doOtherThing();
        Profiler.INSTANCE.pop("thing");
    }

    private static void doOtherThing() throws InterruptedException {
        Profiler.INSTANCE.push("thing2");
        Thread.sleep(2);
        doThirdOtherThing();
        Profiler.INSTANCE.pop("thing2");
    }

    private static void doThirdOtherThing() throws InterruptedException {
        Profiler.INSTANCE.push("thing3");
        Thread.sleep(2);
        Profiler.INSTANCE.pop("thing3");
    }
}
