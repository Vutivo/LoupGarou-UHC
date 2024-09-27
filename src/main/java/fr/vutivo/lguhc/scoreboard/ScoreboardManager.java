package fr.vutivo.lguhc.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.game.UHCgame;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static fr.vutivo.lguhc.game.UHCgame.Chrono;


public class ScoreboardManager implements Listener {
    private final Map<UUID, FastBoard> boards = new HashMap<>() ;
  private final LGUHC main;





    public ScoreboardManager(LGUHC main) {
        this.main = main;





        new BukkitRunnable() {
            @Override
            public void run() {
                for (FastBoard board : boards.values()) {
                    try {
                        if (main.isState(UHCState.WAITTING)) {
                            updateWaitingBoard(board);
                        }
                        if (main.isState(UHCState.GAME)){
                            updateGameBoard(board);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimer(main, 0L, 10L);

    }

  public void Setscoreboard(Player player){

      FastBoard board = new FastBoard(player);
      board.updateTitle("§cLoup-Garou UHC");
      boards.put(player.getUniqueId(),board);

  }


    private void updateWaitingBoard(FastBoard board) {
        try {
            updateBoard(board,
                    "",
                    "&a En attente ...",
                    "",
                    "&7 Joueur: " + main.playerIG + " / " + main.nbRoles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGameBoard(FastBoard board) {
        try {


            updateBoard(board,
                    "episode : &b"+ main.episode,
                    "Joueurs &c"+ main.playerIG,
                    "Groupe de &c"+ main.grouplist,
                    "",
                    "Timer: &c"+Chrono(main.Timer),
                    "Cycle: &b"+ main.Cycle,
                    "",
                    "Bordure: &c"+ main.Border);


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

