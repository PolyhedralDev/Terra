package com.dfsek.terra.cmd;

import com.dfsek.terra.region.Generator;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;

import static picocli.CommandLine.Option;

@SuppressWarnings({"unused", "DefaultAnnotationParam"})
@Command(
        name = "circle",
        separator = "=",
        usageHelpAutoWidth = true,
        version = {
                "Circle Command",
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
        description = "Generates terrain in a circular shape.",
        footerHeading = "%n",
        footer = {
                "This software is licensed under GNU GPLv3. Copyright(c) 2021",
                "The sources can be found on github at https://github.com/PolyhedralDev/Terra.",
                "Please report all bugs and issues to the Terra discord server, or the github issues page."
        },
        commandListHeading = "%nCommands:%n%n",
        resourceBundle = "region"
)
public class CircleGenerationCommand implements Runnable {
    @ParentCommand
    RegionCliInterface cliInterface;

    @Option(names = {"r", "radius"}, required = true, description = "Sets the radius", paramLabel = "<radius>")
    int radius;
    @Option(names = {"x", "xOrigin"}, description = "Sets the origin on the x axis.", paramLabel = "<x>")
    int centerX = 0;
    @Option(names = {"z", "zOrigin"}, description = "Sets the origin on the z axis.", paramLabel = "<z>")
    int centerZ = 0;

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run() {
        Generator generator = cliInterface.buildGenerator();

        for(int i = centerZ - radius; i <= centerZ + radius; i++) {
            // test upper half of circle, stopping when top reached
            for(int j = centerX; (j - centerX) * (j - centerX) + (i - centerZ) * (i - centerZ) <= radius * radius; j--) {
                generator.addChunk(j, i);
            }
            // test bottom half of circle, stopping when bottom reached
            for(int j = centerX + 1; (j - centerZ) * (j - centerZ) + (i - centerZ) * (i - centerZ) <= radius * radius; j++) {
                generator.addChunk(j, i);
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
        return "CircleGenerationCommand{" +
                "cliInterface=" + cliInterface +
                ", radius=" + radius +
                ", x=" + centerX +
                ", z=" + centerZ +
                '}';
    }
}
