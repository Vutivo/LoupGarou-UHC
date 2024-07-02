package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UHCJoin implements Listener {
    private final LGUHC main;
    private final ScoreboardManager board;

    public UHCJoin(LGUHC pl) {
        this.main = pl;
        this.board= new ScoreboardManager(main);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        if(main.world.equals(player.getWorld())){
            player.teleport(main.Spawn);
        }
        Bukkit.broadcastMessage("§7+"+player.getDisplayName()+"(§6"+ main.nbPlayers+"§7/§6"+main.nbRoles+"§7)");
        main.nbPlayers = main.nbPlayers + 1;
        board.Setscoreboard(player);






    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        Bukkit.broadcastMessage("§7-"+player.getDisplayName()+"(§6"+ main.nbPlayers+"§7/§6"+main.nbRoles+"§7)");
        main.nbPlayers = main.nbPlayers - 1;
        board.Removescoreboard(player);

    }
}
