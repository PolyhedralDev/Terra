object Versions {
    object Libraries {
        const val tectonic = "4.2.0"
        const val paralithic = "0.7.0"
        const val strata = "1.1.1"
        
        const val cloud = "1.7.0"
        
        const val slf4j = "1.7.36"
        const val log4j_slf4j_impl = "2.14.1"
        
        object Internal {
            const val apacheText = "1.9"
            const val jafama = "2.3.2"
            const val apacheIO = "2.6"
            const val fastutil = "8.5.6"
        }
    }
    
    object Fabric {
        const val fabricLoader = "0.14.8"
        const val fabricAPI = "0.57.0+1.19"
    }
    
    object Quilt {
        const val quiltLoader = "0.17.0"
        const val fabricApi = "2.0.0-beta.4+0.57.0-1.19"
    }
    
    object Mod {
        const val mixin = "0.11.2+mixin.0.8.5"
        
        const val minecraft = "1.19"
        const val yarn = "$minecraft+build.1"
        const val fabricLoader = "0.14.2"
        
        const val architecuryLoom = "0.12.0.290"
        const val architecturyPlugin = "3.4-SNAPSHOT"
        
        const val loomQuiltflower = "1.7.1"
        
        const val lazyDfu = "0.1.2"
    }
    
    object Forge {
        const val forge = "${Mod.minecraft}-41.0.63"
        const val burningwave = "12.53.0"
    }
    
    object Bukkit {
        const val paper = "1.18.2-R0.1-SNAPSHOT"
        const val paperLib = "1.0.5"
        const val minecraft = "1.19"
        const val reflectionRemapper = "0.1.0-SNAPSHOT"
    }
    
    object Sponge {
        const val sponge = "9.0.0-SNAPSHOT"
        const val mixin = "0.8.2"
        const val minecraft = "1.17.1"
    }
    
    object CLI {
        const val nbt = "6.1"
        const val logback = "1.2.9"
        const val commonsIO = "2.7"
        const val guava = "31.0.1-jre"
    }
}