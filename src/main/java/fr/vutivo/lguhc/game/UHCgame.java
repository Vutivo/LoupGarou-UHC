package fr.vutivo.lguhc.game;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.role.LgRoles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class UHCgame extends BukkitRunnable {
    private LGUHC main;
    public int timer = 0;

    public UHCgame(LGUHC main) {
        this.main = main;
    }

    @Override
    public void run() {

        if (timer == main.timepvp) {
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(main.gameTag +"§b Le pvp vient de s'activer");
            }

        }

        if (timer == main.timerole) {
            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    giveAllRole();
                    for (Joueur joueur : main.getJoueurs()){
                        if(joueur.isConnected()){
                            joueur.sendDesc();
                        }

                    }

                    main.PlayerHasRole = true;

                }
            },20);
        }

        timer++;

    }

    private void giveAllRole() {
        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        Collections.shuffle(players); // Mélange les joueurs pour éviter les biais

        Map<LgRoles, Integer> roleAssignments = new HashMap<>();
        for (LgRoles role : LgRoles.values()) {
            roleAssignments.put(role, role.number); // Initialiser avec les nombres de rôles disponibles
        }

        for (Player player : players) {
            LgRoles role = null;
            while (role == null || roleAssignments.get(role) <= 0) {
                role = LgRoles.values()[new Random().nextInt(LgRoles.values().length)];
            }

            roleAssignments.put(role, roleAssignments.get(role) - 1);
            setRole(player, role);
        }
    }

    private void setRole(Player p, LgRoles role) {

        main.Players.add(new Joueur(p.getUniqueId(), role, role.camp, main));

        main.PlayerInGame.add(p.getUniqueId());


        if (!main.compo.contains(role)) main.compo.add(role);

    }
}
