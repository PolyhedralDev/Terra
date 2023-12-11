object Versions {
    object Libraries {
        const val tectonic = "4.2.1"
        const val paralithic = "0.7.1"
        const val strata = "1.3.2"
        
        const val cloud = "1.8.4"
        
        const val caffeine = "3.1.8"
        
        const val slf4j = "2.0.9"
        const val log4j_slf4j_impl = "2.20.0"
        
        object Internal {
            const val shadow = "8.1.1"
            const val apacheText = "1.11.0"
            const val apacheIO = "2.15.1"
            const val guava = "32.1.3-jre"
            const val asm = "9.6"
            const val snakeYml = "2.2"
            const val jetBrainsAnnotations = "24.1.0"
            const val junit = "5.10.1"
        }
    }
    
    object Fabric {
        const val fabricAPI = "0.91.2+${Mod.minecraft}"
    }
//
//    object Quilt {
//        const val quiltLoader = "0.20.2"
//        const val fabricApi = "7.3.1+0.89.3-1.20.1"
//    }
    
    object Mod {
        const val mixin = "0.12.5+mixin.0.8.5"
        
        const val minecraft = "1.20.4"
        const val yarn = "$minecraft+build.1"
        const val fabricLoader = "0.15.1"
        
        const val architecuryLoom = "1.4.369"
        const val architecturyPlugin = "3.4.151"
    }
//
//    object Forge {
//        const val forge = "${Mod.minecraft}-48.0.13"
//        const val burningwave = "12.63.0"
//    }
    
    object Bukkit {
        const val minecraft = "1.20.4"
        const val paperBuild = "$minecraft-R0.1-20231209.173338-2"
        const val paper = paperBuild
        const val paperLib = "1.0.8"
        const val reflectionRemapper = "0.1.0"
        const val paperDevBundle = paperBuild
        const val runPaper = "2.2.2"
        const val paperWeight = "1.5.11"
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
        const val logback = "1.4.14"
    }
}