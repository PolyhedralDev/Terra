DIRECTORY=./target
PROJECT=Terra
NAME="Terra:DEFAULT"
WORLD=world
REPO=https://github.com/PolyhedralDev/WorldGenTestServer


color() {
    if [ $2 ]; then
            echo -e "\e[$1;$2m"
    else
            echo -e "\e[$1m"
    fi
}
colorend() {
    echo -e "\e[m"
}

if [ ! -d "$DIRECTORY/server" ]; then
  echo "$DIRECTORY/server not found. Cloning now."
  git clone $REPO $DIRECTORY/server
fi
sed -i "s/\${gen}/$NAME/g" $DIRECTORY/server/bukkit.yml
sed -i "s/\${world}/$WORLD/g" $DIRECTORY/server/bukkit.yml
cp $DIRECTORY/prod/$PROJECT.jar $DIRECTORY/server/plugins/$PROJECT.jar
cd $DIRECTORY/server || exit
if ! test -f "paperclip.jar"; then
    echo "Paper not found. Downloading now."
    wget https://papermc.io/api/v1/paper/1.16.3/latest/download -O paperclip.jar
fi
if [ -z "$(grep true eula.txt 2>/dev/null)" ]; then
    echo
    echo "$(color 32)  It appears you have not agreed to Mojangs EULA yet! Press $(color 1 33)y$(colorend) $(color 32)to confirm agreement to"
    read -p "  Mojangs EULA, found at:$(color 1 32) https://account.mojang.com/documents/minecraft_eula $(colorend) " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "$(color 1 31)Aborted$(colorend)"
        exit;
    fi
    echo "eula=true" > eula.txt
fi

java -Xms5G -Xmx5G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC \
-XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 \
-XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 \
-XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true \
-jar paperclip.jar nogui