package fr.vutivo.lguhc;

import fr.vutivo.lguhc.event.EventManager;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class LGUHC extends JavaPlugin {

    public UHCState state;
    public Integer nbPlayers = 0;
    public  Integer nbRoles = 0;

    //Cage
    public boolean cage = true;
    public Integer cageSize = 50;
    public Integer cageHeight = 4;

    public World world;
    public WorldBorder wb;
    public Location Spawn;


    //Color
    public final String RESET = "\u001B[0m";
    public final String BLACK = "\u001B[30m";
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String BLUE = "\u001B[34m";
    public final String PURPLE = "\u001B[35m";
    public final String CYAN = "\u001B[36m";
    public final String WHITE = "\u001B[37m";



    @Override
    public void onEnable() {
        System.out.println(GREEN +"[LOUP-GAROUS] Le plugin est en cours de demarage");
        //Save du config.yml
            saveDefaultConfig();

        world = Bukkit.getWorld(getConfig().getString("Spawn.worldName"));
        Spawn = getLocation(getConfig().getString("Spawn.location"));

        cage = getConfig().getBoolean("Cage.state");
        cageSize = getConfig().getInt("Cage.cageSize");
        cageHeight = getConfig().getInt("Cage.cageHeight");

        if (world != null) {
            world.setPVP(true);
            world.setGameRuleValue("reducedDebugInfo", "true");
            world.setGameRuleValue("naturalRegeneration", "false");
            world.setGameRuleValue("announceAdvancements", "false");
            world.setGameRuleValue("spawnRadius", "0");
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("showDeathMessages", "false");
            world.setDifficulty(Difficulty.HARD);
            wb = world.getWorldBorder();

            if(!cage){
                System.out.println(RED+"LOUP-GAROUS La cage n'a pas ete build");
            }else {
                createCage();
            }

        } else {
            getLogger().severe(RED+"LOUP-GAROUS Impossible de trouver le monde !");
        }

        state = UHCState.WAITTING;
        EventManager.registerEvents(this);


    }

    @Override
    public void onDisable() {
        deleteCage();
        System.out.println(BLUE+"[LOUP-GAROUS] Le plugin s'eteint");
        }
    public void createCage() {
        World world = Bukkit.getWorlds().get(0);
        int startY = 150;

//Plateform
        for (int x = -cageSize / 2; x < cageSize / 2; x++) {
            for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                Block block = world.getBlockAt(x, startY, z);
                block.setType(Material.STAINED_GLASS);

            }
        }
//Wall
        for (int y = startY + 1; y <= startY + cageHeight; y++) {
            for (int x = -cageSize / 2; x < cageSize / 2; x++) {
                for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                    if (x == -cageSize / 2 || x == cageSize / 2 - 1 || z == -cageSize / 2 || z == cageSize / 2 - 1) {
                        Block block = world.getBlockAt(x, y, z);
                        block.setType(Material.STAINED_GLASS_PANE);
                    }
                }
            }
        }


        System.out.println(CYAN+"[LOUP-GAROUS] La cage au spawn a ete genere !");
    }
    public void deleteCage() {
        World world = Bukkit.getWorlds().get(0);
        int startY = 150;

//Plateform
        for (int x = -cageSize / 2; x < cageSize / 2; x++) {
            for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                Block block = world.getBlockAt(x, startY, z);
                block.setType(Material.AIR);

            }
        }
//Wall
        for (int y = startY + 1; y <= startY + cageHeight; y++) {
            for (int x = -cageSize / 2; x < cageSize / 2; x++) {
                for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                    if (x == -cageSize / 2 || x == cageSize / 2 - 1 || z == -cageSize / 2 || z == cageSize / 2 - 1) {
                        Block block = world.getBlockAt(x, y, z);
                        block.setType(Material.AIR);
                    }
                }
            }
        }


    }
    private Location getLocation(String loc) {
        String[] args = loc.split(",");

        double x = Double.valueOf(args[0]);
        double y = Double.valueOf(args[1]);
        double z = Double.valueOf(args[2]);

        return new Location(world, x, y, z);
    }
}
