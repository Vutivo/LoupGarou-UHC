package fr.vutivo.lguhc.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public class ScoreboardManager implements Listener {
    private final Map<UUID, FastBoard> boards = new HashMap<>() ;
  private final LGUHC main;
  private UHCState state;

    public ScoreboardManager(LGUHC pl) {
        this.main = pl;
        this.state = UHCState.WAITTING;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (FastBoard board : boards.values()) {
                    try {
                        if (state == UHCState.WAITTING) {
                            updateWaitingBoard(board);
                        } else {
                            updateOtherStateBoard(board);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimer(pl, 0L, 20L);

    }

  public void Setscoreboard(Player player){

      FastBoard board = new FastBoard(player);
      board.updateTitle("§cLoup-Garou UHC");



      boards.put(player.getUniqueId(),board);

  }
    public void Removescoreboard(Player player){
        FastBoard board = boards.remove(player.getUniqueId());

        if(board!= null){
            board.delete();
        }
    }

    private void updateWaitingBoard(FastBoard board) {
        try {
            updateBoard(board,
                    "",
                    "&a En attente ...",
                    "",
                    "&7 Joueur: " + main.nbPlayers + " / " + main.nbRoles);
            main.nbRoles++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOtherStateBoard(FastBoard board) {
        try {
            // Placeholder method for updating scoreboard in other state
            // Replace with your logic for the other state
            updateBoard(board,
                    "",
                    "&c Autre état ...",
                    "",
                    "&7 Joueur: " + main.nbPlayers + " / " + main.nbRoles);
            main.nbRoles++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBoard(FastBoard board,String... lines){
      for (int x = 0; x < lines.length; ++x){
          lines[x] = ChatColor.translateAlternateColorCodes('&',lines[x]);
      }
        board.updateLines(lines);
    }

  }

