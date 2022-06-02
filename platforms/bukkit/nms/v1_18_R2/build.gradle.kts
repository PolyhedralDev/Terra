dependencies {
    api(project(":platforms:bukkit:common"))
    
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-20220519.005047-123")
    compileOnly(group = "org.spigotmc", name = "spigot", version = "1.18.2-R0.1-SNAPSHOT")
}