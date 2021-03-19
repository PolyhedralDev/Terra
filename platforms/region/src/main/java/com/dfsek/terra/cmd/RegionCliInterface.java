package com.dfsek.terra.cmd;

import com.dfsek.terra.StandalonePlugin;
import com.dfsek.terra.config.WrapperPluginConfig;
import com.dfsek.terra.region.Generator;
import org.slf4j.bridge.SLF4JBridgeHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.io.File;

import static picocli.CommandLine.ParameterException;

@SuppressWarnings({"FieldMayBeFinal", "unused", "DefaultAnnotationParam"})
@Command(
        name = "region",
        usageHelpAutoWidth = true,
        version = {
                "Region Generator: ${bundle:region.version:-Unknown Region Version}",
                "Terra: ${bundle:terra.version:-Unknown Terra Version}",
                "JVM: ${java.version} (${java.vm.name} ${java.vm.version})",
                "OS: ${os.name} ${os.version} ${os.arch}"
        },
        separator = " ",
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
        description = "Generates terrain using the Terra generator, without depending on an implementation of minecraft.",
        footerHeading = "%n",
        footer = {
                "This software is licensed under GNU GPLv3. Copyright(c) 2021",
                "The sources can be found on github at https://github.com/PolyhedralDev/Terra.",
                "Please report all bugs and issues to the Terra discord server, or the github issues page."
        },
        commandListHeading = "%nCommands:%n%n",
        subcommands = {
                CircleGenerationCommand.class,
                RectangleGenerationCommand.class,
                SquareGenerationCommand.class
        },
        resourceBundle = "region"
//        scope = CommandLine.ScopeType.INHERIT
)
public class RegionCliInterface implements Runnable {
    @Option(names = {"-s", "--seed"}, description = "The seed ot use for generation.", paramLabel = "<seed>")
    long seed = 0L;
    @Option(names = {"-d", "--dir", "--directory"}, description = "The directory to generate the files in.", paramLabel = "<directory>", defaultValue = ".")
    File directory;
    @Option(names = "--debug", description = "Enables debug mode for Terra. This will enable structure logging statements.")
    boolean debug = false;
    @Option(names = {"-l", "--language"}, description = "Changes the language for logging statements.", paramLabel = "<language>")
    String language = "en_us";
    @Option(names = "--carver-cache-size", description = "Sets the size for the carver cache.", paramLabel = "<size>")
    int carverCacheSize = 512;
    @Option(names = "--structure-cache-size", description = "Sets the size for the structure cache.", paramLabel = "<size>")
    int structureCacheSize = 128;
    @Option(names = "--sampler-cache-size", description = "Sets the size for the sample cache.", paramLabel = "<size>")
    int samplerCacheSize = 512;
    @Option(names = {"-r", "--max-recursions"}, description = "Sets the maximum number of recursions.", paramLabel = "<recursions>")
    int maxRecursions = 1024;
    @Option(names = {"-p", "--profile", "--profiler"}, description = "Enables the profiler.")
    boolean profiler = false;
    /*
        @Option(names = "--force-file-encoding",
                description = {
                        "Forces the file encoding for the terminal UI. Use either UTF-8 or iso-8859-1.",
                        "Valid values: ${COMPLETION-CANDIDATES}"
                }, paramLabel = "<fileEncoding>")
        FileEncodings encodings = FileEncodings.UTF_8;
    */
    @Option(names = {"-c", "--config", "--config-id"}, description = "Selects which config ID to use for the generator. The ID may be namespaced (eg. \"Terra:DEFAULT\") or not namespaced. (eg. \"DEFAULT\")", paramLabel = "<configId>")
    String configId = "DEFAULT";
    @Option(names = {"-t", "--threads"}, description = "Sets the amount of threads to be used for the generator. Defaults to -1, or as many as needed.", paramLabel = "<threads>")
    int threads = -1;
    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Prints the help menu.", scope = CommandLine.ScopeType.INHERIT)
    boolean help = false;
    @Option(names = {"-v", "--version"}, versionHelp = true, description = "Prints the version.", scope = CommandLine.ScopeType.INHERIT)
    boolean version = false;
    @Spec
    CommandSpec spec;

    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        System.setProperty("picocli.usage.width", "AUTO");
//        new CommandLine(new RegionCliInterface()).usage(System.out);
        int exitCode = new CommandLine(new RegionCliInterface()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Must specify sub command!");
    }

    public Generator buildGenerator() {
        StandalonePlugin plugin = new StandalonePlugin(WrapperPluginConfig.builder()
                .setDebug(debug)
                .setLanguage(language)
                .setCarverCacheSize(carverCacheSize)
                .setStructureCache(structureCacheSize)
                .setSamplerCache(samplerCacheSize)
                .setMaxRecursions(maxRecursions).build());
        return new Generator(seed, plugin);
    }

/*
    public enum FileEncodings {
        UTF_8,
        iso_8859_1
    }
*/
}
