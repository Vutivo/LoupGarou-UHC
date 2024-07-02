package fr.vutivo.lguhc.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import fr.vutivo.lguhc.LGUHC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ScoreboardManager implements Listener {
    private final Map<UUID, FastBoard> boards = new HashMap<>() ;
  private final LGUHC main;

    public ScoreboardManager(LGUHC pl) {
        this.main = pl;
        pl.getServer().getScheduler().scheduleSyncRepeatingTask(pl,()->{
            for(FastBoard board : boards.values()){

                updateBoard(board,
                        "",
                        "&a En attente ...",
                        "",
                        "&7 Joueur:"+main.nbPlayers+" / "+main.nbRoles);
            }

        },0L,20L);
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
    private void updateBoard(FastBoard board,String... lines){
      for (int x = 0; x < lines.length; ++x){
          lines[x] = ChatColor.translateAlternateColorCodes('&',lines[x]);
      }
        board.updateLines(lines);
    }

  }

