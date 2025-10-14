object Versions {
    object Terra {
        const val overworldConfig = "latest"
        const val reimagENDConfig = "latest"
        const val tartarusConfig = "latest"
        const val defaultConfig = "latest"
    }
    
    object Libraries {
        const val tectonic = "4.3.1"
        const val paralithic = "2.0.1"
        const val strata = "1.3.2"
        const val seismic = "2.1.1"
        
        const val cloud = "2.0.0"
        
        const val caffeine = "3.2.2"
        
        const val slf4j = "2.0.17"

        object Internal {
            const val shadow = "8.3.9"
            const val apacheText = "1.14.0"
            const val apacheIO = "2.20.0"
            const val guava = "33.5.0-jre"
            const val asm = "9.9"
            const val snakeYml = "2.5"
            const val jetBrainsAnnotations = "26.0.2-1"
            const val junit = "6.0.0"
            const val nbt = "6.1"
        }
    }
    
    object Fabric {
        const val fabricAPI = "0.134.1+${Mod.minecraft}"
        const val cloud = "2.0.0-beta.13"
    }
//
//    object Quilt {
//        const val quiltLoader = "0.20.2"
//        const val fabricApi = "7.3.1+0.89.3-1.20.1"
//    }
    
    object Mod {
        const val mixin = "0.16.4+mixin.0.8.7"
        const val mixinExtras = "0.5.0"
        
        const val minecraft = "1.21.10"
        const val yarn = "$minecraft+build.1"
        const val fabricLoader = "0.17.2"
        
        const val architecuryLoom = "1.11.451"
        const val architecturyPlugin = "3.4.162"

    }
//
//    object Forge {
//        const val forge = "${Mod.minecraft}-48.0.13"
//        const val burningwave = "12.63.0"
//    }
    
    object Bukkit {
        const val minecraft = "1.21.10"
        const val nms = "$minecraft-R0.1"
        const val paperBuild = "$nms-20251012.013929-7"
        const val paper = paperBuild
        const val paperLib = "1.0.8"
        const val reflectionRemapper = "0.1.3"
        const val paperDevBundle = paperBuild
        const val runPaper = "2.3.1"
        const val paperWeight = "2.0.0-beta.19"
        const val cloud = "2.0.0-beta.12"
        const val multiverse = "5.3.0"
    }
    
//
//    object Sponge {
//        const val sponge = "9.0.0-SNAPSHOT"
//        const val mixin = "0.8.2"
//        const val minecraft = "1.17.1"
//    }
//
    object CLI {
        const val logback = "1.5.19"
        const val picocli = "4.7.7"
    }
    
    object Allay {
        const val api = "0.13.0"
        const val gson = "2.13.2"
        
        const val mappings = "15398c1"
        const val mappingsGenerator = "8fa6058"
        
        const val mcmeta = "e85a17c"
    }
    
    object Minestom {
        const val minestom = "2025.10.04-1.21.8"
    }
}
