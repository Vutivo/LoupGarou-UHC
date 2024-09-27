package fr.vutivo.lguhc.game;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.game.UHCgame;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;


public class UHCStart extends BukkitRunnable {

    private final LGUHC main;
    private Integer Timer = 10;


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


        main.world.setTime(23500);
        main.wb.setSize(main.Border * 2);
        main.deleteCage();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
           main.clearPlayer(player);
            score.Setscoreboard(player);
            giveStartItems(player);



            main.PlayerInGame.add(player.getUniqueId());

            main.randomTp(player);


        }

        game.runTaskTimer(main,0,20);

    }

    private void giveStartItems(Player player){

        if(main.statrtinginv.isEmpty()){
            player.getInventory().setItem(0,new ItemStack(Material.COOKED_BEEF,64));
            player.getInventory().setItem(1,new ItemStack(Material.WATER_BUCKET,1));
            player.getInventory().setItem(2,new ItemStack(Material.BOOK,11));
        }else {
            // Parcours la HashMap et place les items dans l'inventaire du joueur
            for (Map.Entry<Integer, ItemStack> entry : main.statrtinginv.entrySet()) {
                int slot = entry.getKey();         // Le slot dans l'inventaire
                ItemStack item = entry.getValue(); // L'ItemStack à placer
                player.getInventory().setItem(slot-9,item);
            }

        }

    }


}







