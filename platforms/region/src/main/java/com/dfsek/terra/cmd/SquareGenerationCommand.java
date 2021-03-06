package com.dfsek.terra.cmd;

import com.dfsek.terra.region.Generator;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;

@SuppressWarnings({"unused", "DefaultAnnotationParam"})
@Command(
        name = "square",
        separator = "=",
        usageHelpAutoWidth = true,
        version = {
                "Square Command",
                "Region Generator: ${bundle:region.version:-Unknown Region Version}",
                "Terra: ${bundle:terra.version:-Unknown Terra Version}",
                "JVM: ${java.version} (${java.vm.name} ${java.vm.version})",
                "OS: ${os.name} ${os.version} ${os.arch}"
        },
        headerHeading = "",
        synopsisHeading = "Usage:%n",
//        abbreviateSynopsis = true,
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n",
        sortOptions = true,
        exitCodeListHeading = "%nExit Status:%n",
        exitCodeList = {
                "0:No issues. No issues. Exited successfully.",
                "1:Execution resulted in failure.",
                "2:Invalid arguments."
        },
        descriptionHeading = "%nDescription:%n",
        description = "Generates terrain in a square shape.",
        footerHeading = "%n",
        footer = {
                "This software is licensed under GNU GPLv3. Copyright(c) 2021",
                "The sources can be found on github at https://github.com/PolyhedralDev/Terra.",
                "Please report all bugs and issues to the Terra discord server, or the github issues page."
        },
        commandListHeading = "%nCommands:%n%n",
        resourceBundle = "region"
)
public class SquareGenerationCommand implements Runnable {
    @ParentCommand
    RegionCliInterface cliInterface;

    @Option(names = {"s", "size"}, required = true, description = "Sets the size.", paramLabel = "<size>")
    int size;
    @Option(names = {"x", "xOrigin"}, description = "Sets the origin on the x axis.", paramLabel = "<x>")
    int x = 0;
    @Option(names = {"z", "zOrigin"}, description = "Sets the origin on the z axis.", paramLabel = "<z>")
    int z = 0;
    @Option(names = {"c", "centered"}, description = "Sets whether or not the x and z origin are relative to the center or to the top left corner.")
    boolean centered = true;

    @Override
    public void run() {
        Generator generator = cliInterface.buildGenerator();
        if(centered) {
            for(int cx = (-size / 2); cx <= (size / 2); cx++)
                for(int cz = (-size / 2); cz <= (size / 2); cz++)
                    generator.addChunk(cx, cz);
        } else {
            for(int cx = x; cx < size; cx++) {
                for(int cz = z; cz < size; cz++) {
                    generator.addChunk(cx, cz);
                }
            }
        }

        try {
            generator.generate();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "RectangleGenerationCommand{" +
                "cliInterface=" + cliInterface +
                ", size=" + size +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
