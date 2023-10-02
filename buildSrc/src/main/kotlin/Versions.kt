object Versions {
    object Libraries {
        const val tectonic = "4.2.0"
        const val paralithic = "0.7.0"
        const val strata = "1.1.1"
        
        const val cloud = "1.8.4"
        
        const val slf4j = "2.0.9"
        const val log4j_slf4j_impl = "2.20.0"
        
        object Internal {
            const val shadow = "8.1.1"
            const val apacheText = "1.10.0"
            const val jafama = "2.3.2"
            const val apacheIO = "2.14.0"
            const val guava = "32.1.2-jre"
            const val asm = "9.5"
            const val snakeYml = "2.2"
        }
    }
    
    object Fabric {
        const val fabricAPI = "0.89.3+${Mod.minecraft}"
    }
    
    object Quilt {
        const val quiltLoader = "0.20.2"
        const val fabricApi = "7.3.1+0.89.3-1.20.1"
    }
    
    object Mod {
        const val mixin = "0.12.5+mixin.0.8.5"
        
        const val minecraft = "1.20.2"
        const val yarn = "$minecraft+build.2"
        const val fabricLoader = "0.14.22"
        
        const val architecuryLoom = "1.3.357"
        const val architecturyPlugin = "3.4.146"
        
        const val loomVineflower = "1.11.0"
    }
    
    object Forge {
        const val forge = "${Mod.minecraft}-48.0.13"
        const val burningwave = "12.63.0"
    }
    
    object Bukkit {
        const val paper = "1.18.2-R0.1-SNAPSHOT"
        const val paperLib = "1.0.5"
        const val minecraft = "1.20.2"
        const val reflectionRemapper = "0.1.0-SNAPSHOT"
        const val paperDevBundle = "1.20.2-R0.1-SNAPSHOT"
        const val runPaper = "2.2.0"
        const val paperWeight = "1.5.6"
    }
    
    object Sponge {
        const val sponge = "9.0.0-SNAPSHOT"
        const val mixin = "0.8.2"
        const val minecraft = "1.17.1"
    }
    
    object CLI {
        const val nbt = "6.1"
        const val logback = "1.4.11"
    }
}