package fr.vutivo.lguhc.game;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.role.LgCamps;
import fr.vutivo.lguhc.role.LgItems;
import fr.vutivo.lguhc.role.LgRoles;
import fr.vutivo.lguhc.role.use.Salvateur;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class UHCgame extends BukkitRunnable {
    private final LGUHC main;
    private Joueur joueur;
    private int nextEp;
    private int timer;
    private int nextCycle;

    public UHCgame(LGUHC main) {
        this.main = main;
        this.nextEp = main.epTime;
        this.nextCycle = main.timeCycle;
    }


    @Override
    public void run() {
        main.Timer = timer;


        if(timer == main.timelistlg){
            for(Joueur joueur : main.getJoueurs()){
                if(joueur.getCamp().equals(LgCamps.LoupGarou)){
                    joueur.getPlayer().sendMessage(main.gameTag+"§b Vous pouvez maintenant voir la liste des loups-garoux");
                }
            }
        }
        if (timer == main.timepvp) {
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(main.gameTag +"§b Le pvp vient de s'activer");
                main.ispvp = true;
            }

        }
        PermEffect();

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

                }
            },20);
            main.PlayerHasRole = true;
            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    if(main.compo.contains(LgRoles.SALVA)){
                        new Salvateur(main).canProtect();
                    }

                }
            },20);

        }

        if(timer == main.invincibility){
            for(Player player: Bukkit.getOnlinePlayers()){
                player.sendMessage(main.gameTag+"§b Vous êtes plus invincible");

            }
            main.isinvincibility = false;
        }

        if (timer ==nextCycle){
            if (main.Cycle.equals("Jour")){
                Bukkit.broadcastMessage("§b--------§cDebut de la Nuit§b--------");
                nightEffect();
                main.Cycle ="Nuit";

            }else if(main.Cycle.equals("Nuit")){
                Bukkit.broadcastMessage("§b--------§aDebut du Jour§b--------");
                dayEffect();
                main.Cycle ="Jour";


            }
            nextCycle = timer +main.timeCycle;
        }

        if(timer== nextEp){

            Bukkit.broadcastMessage("§b-------- Fin de l'Episode " + main.episode + " --------");
            main.episode ++;
            nextEp = timer +main.epTime;

            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                //POWER
                @Override
                public void run() {
                    if(main.compo.contains(LgRoles.SALVA) && main.episode >= 2){
                        new Salvateur(main).canProtect();
                        for (Joueur j : main.Players) {
                            j.salvation = false;
                        }

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        }
                    }
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
        main.PlayerInGame.clear();
    }

    private void setRole(Player p, LgRoles role) {

        main.Players.add(new Joueur(p.getUniqueId(), role, role.camp, main));

        main.PlayerInGame.add(p.getUniqueId());

        if (!main.compo.contains(role)) main.compo.add(role);
        new LgItems(main.getPlayer(p.getUniqueId())).giveItems();

    }


    public static String Chrono(int seconds) {
        int hours = seconds / 3600;
        int remainingSecondsAfterHours = seconds % 3600;
        int minutes = remainingSecondsAfterHours / 60;
        int remainingSeconds = remainingSecondsAfterHours % 60;

        if (hours < 1){
            return String.format("%02dm %02ds",minutes, remainingSeconds);
        }else{
            return String.format("%dh %02dm %02ds", hours, minutes, remainingSeconds);
        }


    }

    private void nightEffect(){
        for(Joueur joueur: main.getJoueurs()) {
            if(joueur.isConnected()) {
                if (joueur.getCamp().equals(LgCamps.LoupGarou)) {
                    joueur.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

                }
                if (joueur.getRole().equals(LgRoles.AS)) {
                    joueur.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

                }
            }
        }

    }
    private void dayEffect(){
        for(Joueur joueur: main.getJoueurs()) {
            if(joueur.isConnected()){
                if(joueur.getRole().equals(LgRoles.AS)){

                    joueur.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,0,false,false));

                }
                if (joueur.getCamp().equals(LgCamps.LoupGarou)){
                    joueur.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                }
            }

        }


    }
    private void PermEffect() {
        if (main.PlayerHasRole) {
            for (Joueur joueur : main.getJoueurs()) {
                if(!joueur.noPower && joueur.getRole() == LgRoles.ANCIEN){
                    joueur.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                }

                if(joueur.salvation){
                    joueur.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                }
            }
            }

        }
    }
