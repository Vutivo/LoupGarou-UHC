package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {
    public static void registerEvents(LGUHC pl) {

        PluginManager pm = Bukkit.getPluginManager();

        //scoreboard
        pm.registerEvents(new ScoreboardManager(pl),pl);

        pm.registerEvents(new UHCJoin(pl), pl);

    }
}
