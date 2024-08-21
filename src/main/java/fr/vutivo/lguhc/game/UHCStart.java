package fr.vutivo.lguhc.game;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.game.UHCgame;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;


public class UHCStart extends BukkitRunnable {

    private final LGUHC main;
    private Integer Timer = 3;


    public UHCStart(LGUHC main) {
        this.main = main;

    }

    @Override
    public void run() {

        if (main.start == null) {
            main.start = this;
        }

        main.setState(UHCState.STARTING);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(Timer);
        }

        if (Timer == 10 || Timer == 5 || Timer == 4 || Timer == 3 || Timer == 2 || Timer == 1) {
            Bukkit.broadcastMessage(main.gameTag + "§9Début de la partie  dans §6" + Timer + " §9sec");

        }

        if (Timer == 0) {
            cancel();
            Start();
            return;
        }

        Timer--;

    }

    public void StopStarting() {
        cancel();
      main.setState(UHCState.WAITTING);

        Bukkit.broadcastMessage(main.gameTag + "§cAnnulation de la partie");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(0);
        }

        main.start = null;
    }

    private void Start() {
        UHCgame game = new UHCgame(main);
        cancel();
        main.start = null;
        ScoreboardManager score = new ScoreboardManager(main);


        Bukkit.broadcastMessage(main.gameTag + "§6Début de la partie !");
        main.setState(UHCState.GAME);

        main.world.setPVP(false);
        main.world.setTime(23500);
        main.wb.setSize(main.Border * 2);
        main.deleteCage();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.closeInventory();
            player.setLevel(0);
            player.setHealth(20);
            player.setFoodLevel(10);
            score.Setscoreboard(player);


            main.PlayerInGame.add(player.getUniqueId());

            main.randomTp(player);


                if (main.StartInventory.containsKey(player)) {
                    Map<Integer, ItemStack> playerItems = main.StartInventory.get(player);
                    for (Map.Entry<Integer, ItemStack> entry : playerItems.entrySet()) {
                        int slot = entry.getKey();
                        ItemStack item = entry.getValue();
                        if (item != null) {
                            player.getInventory().setItem(slot , item);
                        }
                    }

                }

        }

        game.runTaskTimer(main,0,20);

    }
}






