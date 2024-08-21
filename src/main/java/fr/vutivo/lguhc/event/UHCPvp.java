package fr.vutivo.lguhc.event;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class UHCPvp implements Listener {
    private final LGUHC main;

    public UHCPvp(LGUHC main){
        this.main = main;
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(main.isState(UHCState.WAITTING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event){
        if(main.isState(UHCState.WAITTING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if(main.isState(UHCState.WAITTING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        if(main.isState(UHCState.WAITTING)){
            event.setCancelled(true);
        }
    }
}
