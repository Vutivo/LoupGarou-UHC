package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import fr.vutivo.lguhc.scoreboard.TabList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class UHCJoin implements Listener {
    private final LGUHC main;
    private final ScoreboardManager board;


    public UHCJoin(LGUHC pl) {
        this.main = pl;
        this.board= new ScoreboardManager(main);
        TabList tab = new TabList(main);


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        main.getLogger().info("DEBUG: main.Spawn = " + main.Spawn);
        main.getLogger().info("DEBUG: main.world = " + main.world);
        main.getLogger().info("DEBUG: player.getWorld() = " + player.getWorld());

        try {
            if (main.world.equals(player.getWorld())) {
                player.teleport(main.Spawn);
            }
                main.nbPlayers++;
                Bukkit.broadcastMessage("§7+" + player.getDisplayName() + "(§6" + main.nbPlayers + "§7/§6" + main.nbRoles + "§7)");

                //Scoreboard
                board.Setscoreboard(player);
                //Tablist
            new TabList(main).set(player);

        } catch (Exception ex) {
            main.getLogger().warning("Erreur lors de la téléportation du joueur " + player.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }


        if(player.isOp()|| player.hasPermission("Configurateur")){
            player.setPlayerListName(main.PrefixTab + player.getName());

           ItemStack config = LGUHC.BuildItems(Material.COMMAND,0,"§4Config",null);
            player.getInventory().setItem(2,config);


        }



    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        main.nbPlayers = main.nbPlayers - 1;
        Bukkit.broadcastMessage("§7-"+player.getDisplayName()+"(§6"+ main.nbPlayers+"§7/§6"+main.nbRoles+"§7)");

        board.Removescoreboard(player);

    }



}
