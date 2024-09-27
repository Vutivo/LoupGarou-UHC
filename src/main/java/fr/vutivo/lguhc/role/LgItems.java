package fr.vutivo.lguhc.role;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class LgItems {


    private Joueur joueur;
    private Player player;

    public LgItems(Joueur joueur){
        this.joueur = joueur ;
        this.player= joueur.getPlayer();

    }

    public void giveItems(){

        LgRoles roles = joueur.getRole();

        if(roles == LgRoles.SALVA){
            ItemStack item = new ItemStack(Material.POTION,2);
            Potion pot =new Potion(1);
            pot.setType(PotionType.INSTANT_HEAL);
            pot.setSplash(true);
            pot.apply(item);

            giveItemStack(item);


        }

        if(roles ==LgRoles.SOSO){
            ItemStack item = new ItemStack(Material.POTION,1);
            Potion pot = new Potion(1);
            pot.setType(PotionType.INSTANT_HEAL);
            pot.setSplash(true);
            pot.apply(item);

            ItemStack item2 = new ItemStack(Material.POTION,2);
            Potion pot2 = new Potion(1);
            pot2.setType(PotionType.INSTANT_DAMAGE);
            pot2.setSplash(true);
            pot2.apply(item2);

            ItemStack item3 = new ItemStack(Material.POTION,1);
            Potion pot3 = new Potion(1);
            pot3.setType(PotionType.REGEN);
            pot3.setSplash(true);
            pot3.apply(item3);


            giveItemStack(item);
            giveItemStack(item2);
            giveItemStack(item3);

        }


        if (roles == LgRoles.AS){
            //Sharp 3
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta sharp = (EnchantmentStorageMeta) item.getItemMeta();
            sharp.addStoredEnchant(Enchantment.DAMAGE_ALL,3,true);
            item.setItemMeta(sharp);
            //Protec 3
            ItemStack item2 = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta prot = (EnchantmentStorageMeta) item2.getItemMeta();
            prot.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,3,true);
            item2.setItemMeta(prot);
            //Pick
            ItemStack item3 = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta pick = (EnchantmentStorageMeta) item3.getItemMeta();
            pick.addStoredEnchant(Enchantment.DURABILITY,3,true);
            pick.addStoredEnchant(Enchantment.DIG_SPEED,3,true);
            item3.setItemMeta(pick);


            giveItemStack(item);
            giveItemStack(item2);
            giveItemStack(item3);


        }



    }

    private void giveItemStack(ItemStack it) {
        if (isInventoryFull()) player.getWorld().dropItem(player.getLocation(), it);
        else player.getInventory().addItem(it);
    }
    private boolean isInventoryFull() {
        return player.getInventory().firstEmpty() == -1;
    }

}
