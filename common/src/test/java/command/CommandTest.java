package command;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.inject.SwitchTarget;
import com.dfsek.terra.api.command.arg.DoubleArgumentParser;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.command.exception.InvalidArgumentsException;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.platform.CommandSender;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class CommandTest {
    @Test
    public void subcommand() throws CommandException {
        CommandManager manager = new TerraCommandManager(null);
        manager.register("test", DemoParentCommand.class);

        manager.execute("test", null, Arrays.asList("subcommand1", "first", "2"));
        manager.execute("test", null, Arrays.asList("subcommand2", "first", "2"));
        manager.execute("test", null, Arrays.asList("s1", "first", "2", "3.4"));
        manager.execute("test", null, Arrays.asList("s2", "first", "2"));
        manager.execute("test", null, Arrays.asList("sub1", "first", "2", "3.4"));
        manager.execute("test", null, Arrays.asList("sub2", "first", "2"));
        manager.execute("test", null, Arrays.asList("first", "2")); // Parent command args
    }

    @Test
    public void args() throws CommandException {
        CommandManager manager = new TerraCommandManager(null);
        manager.register("test", DemoCommand.class);

        manager.execute("test", null, Arrays.asList("first", "2"));
        manager.execute("test", null, Arrays.asList("first", "2", "3.4"));
    }

    @Test
    public void argsBeforeFlags() throws CommandException {
        CommandManager manager = new TerraCommandManager(null);
        manager.register("test", DemoCommand.class);

        try {
            manager.execute("test", null, Arrays.asList("first", "-flag", "2"));
            fail();
        } catch(InvalidArgumentsException ignore) {
        }
    }

    @Test
    public void requiredArgsFirst() throws CommandException {
        CommandManager manager = new TerraCommandManager(null);
        manager.register("test", DemoInvalidCommand.class);

        try {
            manager.execute("test", null, Arrays.asList("first", "2"));
            fail();
        } catch(MalformedCommandException ignore) {
        }
    }

    @Test
    public void switches() throws CommandException {
        CommandManager manager = new TerraCommandManager(null);
        manager.register("test", DemoSwitchCommand.class);

        manager.execute("test", null, Arrays.asList("first", "2"));
        manager.execute("test", null, Arrays.asList("first", "2", "3.4"));

        manager.execute("test", null, Arrays.asList("first", "2", "-a"));
        manager.execute("test", null, Arrays.asList("first", "2", "3.4", "-b"));

        manager.execute("test", null, Arrays.asList("first", "2", "-aSwitch"));
        manager.execute("test", null, Arrays.asList("first", "2", "3.4", "-bSwitch"));

        manager.execute("test", null, Arrays.asList("first", "2", "-aSwitch", "-b"));
        manager.execute("test", null, Arrays.asList("first", "2", "3.4", "-bSwitch", "-a"));
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1", argumentParser = IntegerArgumentParser.class),
                    @Argument(value = "arg2", required = false, argumentParser = DoubleArgumentParser.class)
            }
    )
    public static final class DemoCommand implements CommandTemplate {

        @ArgumentTarget("arg0")
        private String arg0;

        @ArgumentTarget("arg1")
        private Integer arg1;

        @ArgumentTarget("arg2")
        private Double arg2;


        @Override
        public void execute(CommandSender sender) {
            System.out.println(arg0);
            System.out.println(arg1);
            System.out.println(arg2);
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1"),
                    @Argument(value = "arg2", required = false)
            },
            switches = {
                    @Switch(value = "a", aliases = {"aSwitch"}),
                    @Switch(value = "b", aliases = {"bSwitch"})
            }
    )
    public static final class DemoSwitchCommand implements CommandTemplate {
        @ArgumentTarget("arg0")
        private String arg0;

        @ArgumentTarget("arg1")
        private String arg1;

        @ArgumentTarget("arg2")
        private String arg2;

        @SwitchTarget("a")
        private boolean a;

        @SwitchTarget("b")
        private boolean b;


        @Override
        public void execute(CommandSender sender) {
            System.out.println(arg0);
            System.out.println(arg1);
            System.out.println(arg2);

            System.out.println("A: " + a);
            System.out.println("B: " + b);
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg2", required = false), // optional arguments must be last. this command is invalid.
                    @Argument(value = "arg1")
            }
    )
    public static final class DemoInvalidCommand implements CommandTemplate {

        @Override
        public void execute(CommandSender sender) {
            throw new Error("this should never be reached");
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1"),
                    @Argument(value = "arg2", required = false),
            },
            subcommands = {
                    @Subcommand(
                            value = "subcommand1",
                            aliases = {"s1", "sub1"},
                            clazz = DemoChildCommand.class
                    ),
                    @Subcommand(
                            value = "subcommand2",
                            aliases = {"s2", "sub2"},
                            clazz = DemoChildCommand.class // Duplicate command intentional.
                    )
            }
    )
    public static final class DemoParentCommand implements CommandTemplate {
        @ArgumentTarget("arg0")
        private String arg0;

        @ArgumentTarget("arg1")
        private String arg1;

        @ArgumentTarget("arg2")
        private String arg2;


        @Override
        public void execute(CommandSender sender) {
            System.out.println(arg0);
            System.out.println(arg1);
            System.out.println(arg2);
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1"),
                    @Argument(value = "arg2", required = false),
            }
    )
    public static final class DemoChildCommand implements CommandTemplate {
        @ArgumentTarget("arg0")
        private String arg0;

        @ArgumentTarget("arg1")
        private String arg1;

        @ArgumentTarget("arg2")
        private String arg2;


        @Override
        public void execute(CommandSender sender) {
            System.out.println(arg0);
            System.out.println(arg1);
            System.out.println(arg2);
        }
    }
}
