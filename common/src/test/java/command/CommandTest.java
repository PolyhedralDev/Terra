package command;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.ExecutionState;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.commands.StructureCommand;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class CommandTest {
    @Test
    public void subcommand() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("structure", StructureCommand.class);

        manager.execute("structure", Arrays.asList("export"));
    }

    @Test
    public void args() throws CommandException {
        CommandManager manager = new TerraCommandManager();
        manager.register("test", DemoCommand.class);

        manager.execute("test", Arrays.asList("first", "2"));
        manager.execute("test", Arrays.asList("first", "2", "3.4"));
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

    @Command(arguments = {
            @Argument(value = "arg0"),
            @Argument(value = "arg1", type = int.class),
            @Argument(value = "arg2", type = double.class, required = false)
    })
    public static final class DemoCommand implements CommandTemplate {

        @Override
        public void execute(ExecutionState state) {
            System.out.println(state.getArgument("arg0", String.class));
            System.out.println(state.getArgument("arg1", String.class));
            try {
                System.out.println(state.getArgument("arg2", String.class));
            } catch(IllegalArgumentException e) {
                System.out.println("arg2 undefined.");
            }
        }
    }

    @Command(arguments = {
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
}
