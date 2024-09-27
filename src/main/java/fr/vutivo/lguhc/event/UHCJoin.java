package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.role.LgRoles;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import fr.vutivo.lguhc.scoreboard.TabList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class UHCJoin implements Listener {
    private final LGUHC main;
    private final ScoreboardManager board;
    private final TabList tab;



    public UHCJoin(LGUHC pl) {
        this.main = pl;
        this.board= new ScoreboardManager(main);
        this.tab = new TabList(main);


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);


        //Scoreboard
        board.Setscoreboard(player);
        //Tablist
        tab.set(player);

        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
           main.clearPlayer(player);

            try {
                if (main.world.equals(player.getWorld())) {
                    player.teleport(main.Spawn);
                }
                main.playerIG++;
                Bukkit.broadcastMessage("§7+" + player.getDisplayName() + "(§6" + main.playerIG + "§7/§6" + main.nbRoles + "§7)");



            } catch (Exception ex) {
                main.getLogger().warning("Erreur lors de la téléportation du joueur " + player.getName() + ": " + ex.getMessage());
                ex.printStackTrace();

            }

            if (main.host.contains(player)) {
                player.setPlayerListName(main.PrefixTab + player.getName());

                ItemStack config = main.BuildItems(Material.COMMAND, 0, 1, "§4Config");
                player.getInventory().setItem(4, config);
            }

        } else {
            if (main.getPlayer(player.getUniqueId()) ==null) {
                player.sendMessage(main.gameTag + " La partie à deja commancer");
                player.setGameMode(GameMode.SPECTATOR);
            }


        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (main.isState(UHCState.WAITTING)) {
            main.playerIG = main.playerIG - 1;
            Bukkit.broadcastMessage("§7-"+player.getDisplayName()+"(§6"+ main.playerIG+"§7/§6"+main.nbRoles+"§7)");
        }

    }

}
