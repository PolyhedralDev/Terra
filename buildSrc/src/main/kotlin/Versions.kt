object Versions {
    object Terra {
        const val overworldConfig = "v1.3.4"
    }
    
    object Libraries {
        const val tectonic = "4.2.1"
        const val paralithic = "0.7.1"
        const val strata = "1.3.2"
        
        const val cloud = "2.0.0"
        const val cloudPaper = "2.0.0-beta.10"
        const val cloudFabric = "2.0.0-beta.9"
        
        const val caffeine = "3.1.8"
        
        const val slf4j = "2.0.16"

        object Internal {
            const val shadow = "8.3.1"
            const val apacheText = "1.12.0"
            const val apacheIO = "2.16.1"
            const val guava = "33.3.0-jre"
            const val asm = "9.7"
            const val snakeYml = "2.3"
            const val jetBrainsAnnotations = "24.1.0"
            const val junit = "5.11.0"
        }
    }
    
    object Fabric {
        const val fabricAPI = "0.104.0+${Mod.minecraft}"
    }
//
//    object Quilt {
//        const val quiltLoader = "0.20.2"
//        const val fabricApi = "7.3.1+0.89.3-1.20.1"
//    }
    
    object Mod {
        const val mixin = "0.15.3+mixin.0.8.7"
        
        const val minecraft = "1.21.1"
        const val yarn = "$minecraft+build.3"
        const val fabricLoader = "0.16.5"
        
        const val architecuryLoom = "1.7.413"
        const val architecturyPlugin = "3.4.159"
    }
//
//    object Forge {
//        const val forge = "${Mod.minecraft}-48.0.13"
//        const val burningwave = "12.63.0"
//    }
    
    object Bukkit {
        const val minecraft = "1.21.1"
        const val paperBuild = "$minecraft-R0.1-20240917.151311-80"
        const val paper = paperBuild
        const val paperLib = "1.0.8"
        const val reflectionRemapper = "0.1.1"
        const val paperDevBundle = paperBuild
        const val runPaper = "2.3.1"
        const val paperWeight = "1.7.2"
    }
    
    //
//    object Sponge {
//        const val sponge = "9.0.0-SNAPSHOT"
//        const val mixin = "0.8.2"
//        const val minecraft = "1.17.1"
//    }
//
    object CLI {
        const val nbt = "6.1"
        const val logback = "1.5.8"
    }
}