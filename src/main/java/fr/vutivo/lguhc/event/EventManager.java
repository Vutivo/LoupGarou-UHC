package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.gui.ConfigGui;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import fr.vutivo.lguhc.scoreboard.TabList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    public static void registerEvents(LGUHC pl){

        PluginManager pm = Bukkit.getPluginManager();

        //scoreboard & Tablist
        pm.registerEvents(new ScoreboardManager(pl),pl);
        pm.registerEvents(new TabList(pl),pl);

        //Gui
        pm.registerEvents(new ConfigGui(pl),pl);

        //event
        pm.registerEvents(new UHCPvp(pl),pl);
        pm.registerEvents(new UHCJoin(pl), pl);

        //scénario

    }
}
