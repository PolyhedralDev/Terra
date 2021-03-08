package command;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.ExecutionState;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.command.exception.InvalidArgumentsException;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class CommandTest {
    @Test
    public void subcommand() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("test", DemoParentCommand.class);

        manager.execute("test", Arrays.asList("subcommand1", "first", "2"));
        manager.execute("test", Arrays.asList("subcommand2", "first", "2"));
        manager.execute("test", Arrays.asList("s1", "first", "2", "3.4"));
        manager.execute("test", Arrays.asList("s2", "first", "2"));
        manager.execute("test", Arrays.asList("sub1", "first", "2", "3.4"));
        manager.execute("test", Arrays.asList("sub2", "first", "2"));
        manager.execute("test", Arrays.asList("first", "2")); // Parent command args
    }

    @Test
    public void args() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("test", DemoCommand.class);

        manager.execute("test", Arrays.asList("first", "2"));
        manager.execute("test", Arrays.asList("first", "2", "3.4"));
    }

    @Test
    public void argsBeforeFlags() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("test", DemoCommand.class);

        try {
            manager.execute("test", Arrays.asList("first", "-flag", "2"));
            fail();
        } catch(InvalidArgumentsException ignore) {
        }
    }

    @Test
    public void requiredArgsFirst() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("test", DemoInvalidCommand.class);

        try {
            manager.execute("test", Arrays.asList("first", "2"));
            fail();
        } catch(MalformedCommandException ignore) {
        }
    }

    @Test
    public void switches() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("test", DemoSwitchCommand.class);

        manager.execute("test", Arrays.asList("first", "2"));
        manager.execute("test", Arrays.asList("first", "2", "3.4"));

        manager.execute("test", Arrays.asList("first", "2", "-a"));
        manager.execute("test", Arrays.asList("first", "2", "3.4", "-b"));

        manager.execute("test", Arrays.asList("first", "2", "-aSwitch"));
        manager.execute("test", Arrays.asList("first", "2", "3.4", "-bSwitch"));

        manager.execute("test", Arrays.asList("first", "2", "-aSwitch", "-b"));
        manager.execute("test", Arrays.asList("first", "2", "3.4", "-bSwitch", "-a"));
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1", type = int.class),
                    @Argument(value = "arg2", type = double.class, required = false)
            })
    public static final class DemoCommand implements CommandTemplate {

        @Override
        public void execute(ExecutionState state) {
            System.out.println(state.getArgument("arg0", String.class));
            System.out.println(state.getArgument("arg1", int.class));
            try {
                System.out.println(state.getArgument("arg2", double.class));
            } catch(IllegalArgumentException e) {
                System.out.println("arg2 undefined.");
            }
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1", type = int.class),
                    @Argument(value = "arg2", type = double.class, required = false)
            },
            switches = {
                    @Switch(value = "a", aliases = {"aSwitch"}),
                    @Switch(value = "b", aliases = {"bSwitch"})
            }
    )
    public static final class DemoSwitchCommand implements CommandTemplate {

        @Override
        public void execute(ExecutionState state) {
            System.out.println(state.getArgument("arg0", String.class));
            System.out.println(state.getArgument("arg1", int.class));
            try {
                System.out.println(state.getArgument("arg2", double.class));
            } catch(IllegalArgumentException e) {
                System.out.println("arg2 undefined.");
            }

            System.out.println("A: " + state.hasSwitch("a"));
            System.out.println("B: " + state.hasSwitch("b"));
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg2", type = double.class, required = false), // optional arguments must be last. this command is invalid.
                    @Argument(value = "arg1", type = int.class)
            })
    public static final class DemoInvalidCommand implements CommandTemplate {

        @Override
        public void execute(ExecutionState state) {
            throw new Error("this should never be reached");
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1", type = int.class),
                    @Argument(value = "arg2", type = double.class, required = false),
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
            })
    public static final class DemoParentCommand implements CommandTemplate {

        @Override
        public void execute(ExecutionState state) {
            System.out.println("Parent command");
            System.out.println(state.getArgument("arg0", String.class));
            System.out.println(state.getArgument("arg1", int.class));
            try {
                System.out.println(state.getArgument("arg2", double.class));
            } catch(IllegalArgumentException e) {
                System.out.println("arg2 undefined.");
            }
        }
    }

    @Command(
            arguments = {
                    @Argument(value = "arg0"),
                    @Argument(value = "arg1", type = int.class),
                    @Argument(value = "arg2", type = double.class, required = false),
            })
    public static final class DemoChildCommand implements CommandTemplate {
        @Override
        public void execute(ExecutionState state) {
            System.out.println("Child command");
            System.out.println(state.getArgument("arg0", String.class));
            System.out.println(state.getArgument("arg1", int.class));
            try {
                System.out.println(state.getArgument("arg2", double.class));
            } catch(IllegalArgumentException e) {
                System.out.println("arg2 undefined.");
            }
        }
    }
}
