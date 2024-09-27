package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.role.LgCamps;
import fr.vutivo.lguhc.role.LgRoles;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class UHCPvp implements Listener {

    private LGUHC main;
    public UHCPvp(LGUHC main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
            event.setCancelled(true);
        }
        else if (main.isState(UHCState.GAME)) {
            if (!main.ispvp) {
                event.setCancelled(true);
            }
            else {
                event.setCancelled(false);

                boolean isforce = false;
                boolean hasEffects = false;
                double force = 1.0; // Commence à 1 pour les dégâts normaux
                double resistance = 1.0; // Commence à 1 pour les dégâts normaux

                // Récupère les dégâts bruts de base (sans effets)
                double baseDamage = event.getDamage();
                Bukkit.broadcastMessage("Initial damage: " + baseDamage);





                // Vérifie les effets de potion de l'attaquant
                if (attacker instanceof LivingEntity) {
                    LivingEntity livingAttacker = (LivingEntity) attacker;
                    for (PotionEffect effect : livingAttacker.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                            int amplifier = effect.getAmplifier();
                            force += (amplifier + 1) * (main.strength / 100.0); // Ajuste la force
                            hasEffects = true;
                            isforce = true;
                        }
                    }
                }

                // Vérifie les effets de potion de la victime
                if (victim instanceof LivingEntity) {
                    LivingEntity livingVictim = (LivingEntity) victim;
                    for (PotionEffect effect : livingVictim.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
                            int amplifier = effect.getAmplifier();
                            resistance -= (amplifier + 1) * (main.resistance / 100.0); // Ajuste la résistance
                            if (resistance < 0) resistance = 0;
                            hasEffects = true;
                        }
                    }
                }

                if (!hasEffects) {
                    // Pas d'effets, dégâts normaux
                    event.setDamage(baseDamage);
                    return;
                } else {
                    double damage = baseDamage ;
                    if(isforce){
                         damage = baseDamage /2.3;
                        Bukkit.broadcastMessage("damage: " + damage);

                    }

                    double damageAfterForce = damage * force;
                    Bukkit.broadcastMessage("Damage after force: " + damageAfterForce);
                    // Assure que la résistance n'annule pas complètement les dégâts
                    if (resistance >1) resistance = 1;


                    double finalDamage = damageAfterForce * resistance;


                    // Assure un minimum de dégâts
                    if (finalDamage < 0.5) {
                        finalDamage = 0.5;
                    }


                    // Calcul des dégâts ajustés



                    // Application des dégâts ajustés
                    event.setDamage(finalDamage);

                    Bukkit.broadcastMessage("Final damage: " + finalDamage);






                }


            }


        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if(event instanceof EntityDamageByEntityEvent) return;

        Player player = (Player) event.getEntity();

        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
            event.setCancelled(true);
            return;
        } else if (main.isState(UHCState.GAME)) {
            event.setCancelled(main.isinvincibility);

            //no fall

            if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {

                if(main.damageFall.contains(player)){
                    event.setCancelled(true);
                }

                if(main.hasRole(player)){
                    if(main.getPlayer(player.getUniqueId()).salvation){
                        event.setCancelled(true);
                        return;
                    }





                }

            }

        }




    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player player = e.getEntity().getPlayer();
        Location loc = e.getEntity().getPlayer().getLocation();
        Player killer = e.getEntity().getKiller();


        player.spigot().respawn();

        if(main.isState(UHCState.GAME)) {
            if(!main.PlayerHasRole){
               InstanteRevive(player);

            }


           else {

                Joueur victim = main.getPlayer(player.getUniqueId());
                Joueur tueur = null;
                if (killer != null) {
                    tueur = main.getPlayer(killer.getUniqueId());
                    tueur.addKills();
                }


                //Ancien
                if (victim.getRole() == LgRoles.ANCIEN && victim.getPower() ==1) {
                    if (tueur != null && tueur.getCamp() == LgCamps.LoupGarou) {
                        player.sendMessage(main.gameTag + "§bVous avez survécu à une attaque de loup,malheureusement vous avez perdu votre effet de resistance");
                        victim.noPower = true;
                        victim.setPower(0);
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        main.randomTp(player);
                        return;
                    }

                }

                //IDV
                if(victim.getRole() == LgRoles.IDV && victim.getPower() == 1){
                    if (tueur != null && (tueur.getCamp() == LgCamps.Village || tueur.getCamp() == LgCamps.Assassin)) {
                        victim.setPower(0);
                        player.setMaxHealth(16);
                        for(Joueur joueurs : main.getJoueurs()){
                            joueurs.getPlayer().sendMessage(main.gameTag+"§bLe joueur §a"+player.getName() + "§b à été tuer par un membre du village ! il pert donc 2 coeurs permanent");
                        }
                        return;
                    }

                }

                //Autre roles
                main.getPlayer(player.getUniqueId()).setDying(true);
                player.teleport(main.Spawn);
                player.setGameMode(GameMode.SPECTATOR);

                // Exécute la tâche pour gérer la mort
                UHCDeaths task;
                if (killer == null || !(killer instanceof Player)) {
                    task = new UHCDeaths(main, player.getUniqueId(), null, loc, inv(player));
                } else {
                    task = new UHCDeaths(main, player.getUniqueId(), killer.getUniqueId(), loc, inv(player));
                }
                task.runTaskTimer(main, 0, 20);
            }
        }

    }


    private List<ItemStack> inv(Player player) {

        List<ItemStack> inv = new ArrayList<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) inv.add(item);

        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) inv.add(item);
        }
        return inv;
    }


    private void InstanteRevive(Player player){
        player.sendMessage(main.gameTag+"§c Vous venez de vous faire réanimé !");
        main.randomTp(player);
}




}
