package com.dfsek.terra.bukkit.command.command.structure.load;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.SpawnCheckBuilder;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.command.DebugCommand;
import org.apache.commons.io.IOUtils;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LoadRawCommand extends LoadCommand implements DebugCommand {
    public LoadRawCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    private static void setTerraSign(Sign sign, String data) {
        sign.setLine(0, "[TERRA]");
        if(data.length() > 16) {
            sign.setLine(2, data.substring(0, 16));
            sign.setLine(3, data.substring(16));
        } else sign.setLine(2, data);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            Parser parser = new Parser(IOUtils.toString(new FileInputStream(new File(getMain().getDataFolder(), "test.tesf"))));
            parser.addFunction("block", new BlockFunctionBuilder(getMain()));
            parser.addFunction("check", new SpawnCheckBuilder(getMain()));
            Block main = parser.parse();
            main.apply(new Location(new BukkitWorld(sender.getWorld()), sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ()));

        } catch(IOException | ParseException e) {
            e.printStackTrace();
        }
        /*
        try {
            WorldHandle handle = ((TerraBukkitPlugin) getMain()).getWorldHandle();
            Structure struc = Structure.load(new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            StructureInfo info = struc.getStructureInfo();
            int centerX = info.getCenterX();
            int centerZ = info.getCenterZ();
            for(StructureContainedBlock[][] level0 : struc.getRawStructure()) {
                for(StructureContainedBlock[] level1 : level0) {
                    for(StructureContainedBlock block : level1) {
                        Location bLocation = sender.getLocation().add(block.getX() - centerX, block.getY(), block.getZ() - centerZ);
                        if(!block.getPull().equals(StructureContainedBlock.Pull.NONE)) {
                            handle.setBlockData(bLocation.getBlock(), Material.OAK_SIGN.createBlockData(), false);
                            Sign sign = (Sign) bLocation.getBlock().getState();
                            sign.setLine(1, "[PULL=" + block.getPull() + "_" + block.getPullOffset() + "]");
                            String data = block.getBlockData().getAsString(true);
                            setTerraSign(sign, data);
                            sign.update();
                        } else if(!block.getRequirement().equals(StructureSpawnRequirement.BLANK)) {
                            handle.setBlockData(bLocation.getBlock(), Material.OAK_SIGN.createBlockData(), false);
                            Sign sign = (Sign) bLocation.getBlock().getState();
                            sign.setLine(1, "[SPAWN=" + block.getRequirement() + "]");
                            String data = block.getBlockData().getAsString(true);
                            setTerraSign(sign, data);
                            sign.update();
                        } else {
                            handle.setBlockData(bLocation.getBlock(), block.getBlockData(), false);
                            if(block.getState() != null) {
                                block.getState().getState(bLocation.getBlock().getState()).update(true, false);
                            }
                        }
                    }
                }
            }

            for(int y = 0; y < struc.getStructureInfo().getSizeY(); y++) {
                StructureContainedBlock block = struc.getRawStructure()[centerX][centerZ][y];
                if(block.getRequirement().equals(StructureSpawnRequirement.BLANK) && block.getPull().equals(StructureContainedBlock.Pull.NONE)) {
                    Location bLocation = sender.getLocation().add(block.getX() - centerX, block.getY(), block.getZ() - centerZ);
                    handle.setBlockData(bLocation.getBlock(), Material.OAK_SIGN.createBlockData(), false);
                    Sign sign = (Sign) bLocation.getBlock().getState();
                    sign.setLine(1, "[CENTER]");
                    String data = block.getBlockData().getAsString(true);
                    setTerraSign(sign, data);
                    sign.update();
                    break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            LangUtil.send("command.structure.invalid", sender, args[0]);
        }

         */
        return true;
    }

    @Override
    public String getName() {
        return "raw";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            return getStructureNames().stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
