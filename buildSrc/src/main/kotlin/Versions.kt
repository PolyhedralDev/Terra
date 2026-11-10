object Versions {
    object Terra {
        const val overworldConfig = "latest"
        const val reimagENDConfig = "latest"
        const val tartarusConfig = "latest"
        const val defaultConfig = "latest"
    }
    
    object Libraries {
        const val tectonic = "4.4.0"
        const val paralithic = "2.0.1"
        const val strata = "1.3.2"
        const val seismic = "2.5.7"
        
        const val cloud = "2.1.0"
        
        const val caffeine = "3.3.1"
        
        const val slf4j = "2.0.20"

        object Internal {
            const val shadow = "9.6.1"
            const val apacheText = "1.15.0"
            const val apacheIO = "2.22.0"
            const val guava = "33.8.0-jre"
            const val asm = "9.11.1"
            const val snakeYml = "2.7"
            const val jetBrainsAnnotations = "26.1.0"
            const val junit = "6.1.3"
            const val nbt = "6.1"
        }
    }
    
    object Fabric {
        const val fabricAPI = "0.166.0+${Mod.minecraft}"
        const val cloud = "2.1.0"
    }
//
//    object Quilt {
//        const val quiltLoader = "0.20.2"
//        const val fabricApi = "7.3.1+0.89.3-1.20.1"
//    }
    
    object Mod {
        const val mixin = "0.17.5+mixin.0.8.7"
        const val mixinExtras = "0.5.5"
        
        const val minecraft = "26.3"
        const val fabricLoader = "0.20.1"
        
        const val architecturyLoom = "1.17.493"
        const val architecturyPlugin = "3.5.170"

    }
//
//    object Forge {
//        const val forge = "${Mod.minecraft}-48.0.13"
//        const val burningwave = "12.63.0"
//    }
    
    object Bukkit {
        const val minecraft = "26.3"
        const val paperBuild = "$minecraft.build.246-stable"
        const val paper = paperBuild
        const val runPaper = "3.1.0"
        const val wyck = "4.0.0-0f573ad7"
        const val cloud = "2.0.1"
        const val multiverse = "5.8.2"
    }
    
//
//    object Sponge {
//        const val sponge = "9.0.0-SNAPSHOT"
//        const val mixin = "0.8.2"
//        const val minecraft = "1.17.1"
//    }
//
    object CLI {
        const val logback = "1.6.6"
        const val picocli = "4.7.7"
    }
    
    object Allay {
        const val api = "0.28.0"
        const val gson = "2.13.2"
        
        const val mappings = "0c2ac22"
        const val mappingsGenerator = "3742321"
        
        const val mcmeta = "805b8ac"
    }
    
    object Minestom {
        const val minestom = "26_3-SNAPSHOT"
    }
}
