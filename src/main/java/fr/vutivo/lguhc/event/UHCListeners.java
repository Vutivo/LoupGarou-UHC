package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.event.entity.EntityDamageEvent.*;

public class UHCListeners implements Listener {
    private final LGUHC main;


    public UHCListeners(LGUHC main) {
        this.main = main;
    }

@EventHandler
public void onPortal(PlayerPortalEvent event){
        event.setCancelled(true);
}
    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
            if(main.host.contains(player)){
               event.setCancelled(false);
            }
            else{
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
            if(main.host.contains(player)){
                event.setCancelled(false);
            }
            else{
                event.setCancelled(true);
            }
        }
    }

@EventHandler
public void onWeatherChange(WeatherChangeEvent e){
        e.setCancelled(true);
}
    @EventHandler
    public void onThunderChange(ThunderChangeEvent e) {
        e.setCancelled(true);
    }

}























