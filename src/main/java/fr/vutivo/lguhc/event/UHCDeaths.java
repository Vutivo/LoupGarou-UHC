package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import fr.vutivo.lguhc.role.LgRoles;
import fr.vutivo.lguhc.role.use.Sorciere;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UHCDeaths extends BukkitRunnable {

    private int timer = 10;
    private LGUHC main;
    private Location loc;
    private List<ItemStack> inv;
    private UUID killer;
    private UUID victim;


    public UHCDeaths(LGUHC main,UUID victim, UUID killer, Location loc, List<ItemStack> inv) {
        this.main = main;
        this.victim = victim;
        this.loc = loc;
        this.inv = inv;
        this.killer = killer;




    }

    @Override
    public void run() {
        if(main.getPlayer(victim).isDead()){
            cancel();
            return;
        }

        timer --;

        if(timer == 6) {
            Joueur j = main.getPlayer(victim);
            new Sorciere(main).ReaMsg(j.getPlayer());


        }
        if(timer ==0){
            cancel();
            if(main.getPlayer(victim).isDying()){
                death();
            }

        }

    }

    public void death() {

        if (main.PlayerHasRole) {
            if (main.hasRole(victim)) {
                death(victim,loc,inv,killer);

            }
        }
    }

    private void death(UUID victim,Location loc,List<ItemStack> inv,UUID killer){
        Joueur joueurv = main.getPlayer(victim);
        Joueur joueurk = (killer != null) ? main.getPlayer(killer) : null;




        dropInventory(victim,loc,inv);
        joueurv.setDead(true);
        joueurv.setDying(false);

        if (joueurk != null && joueurk.getRole() == LgRoles.LGGrimeur && joueurk.getPower() == 1) {
            joueurk.setPower(0);

            Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
            Bukkit.broadcastMessage("§2§lLe village à perdu un de ses membres ");
            Bukkit.broadcastMessage("§2§l"+joueurv.getName()+" qui était : Loup-garou");
            Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
            joueurk.getPlayer().sendMessage(main.gameTag+"§b le role du joueur "+joueurv.getName()+ " était :" +joueurv.getRole().name);


        }else {
            Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
            Bukkit.broadcastMessage("§2§lLe village à perdu un de ses membres ");
            Bukkit.broadcastMessage("§2§l"+joueurv.getName()+" qui était : "+joueurv.getRole().name);
            Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
        }







    }
    private void dropInventory(UUID victim, Location loc, List<ItemStack> inv){
        if(Bukkit.getPlayer(victim) != null){
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta)head.getItemMeta();

            meta.setOwner(Bukkit.getPlayer(victim).getName());
            meta.setDisplayName("§6Tête de §c" + Bukkit.getPlayer(victim).getName());

            meta.setLore(Arrays.asList("§bCette tête a été récupéré ", "§bsur un joueur" ));
            head.setItemMeta(meta);
            loc.getWorld().dropItemNaturally(loc, head);
        }

        if(!inv.isEmpty()){
            for (ItemStack it : inv){
                if (it != null && it.getType() != Material.AIR) {
                    loc.getWorld().dropItemNaturally(loc, it);
                }
            }
        }
    }







}
