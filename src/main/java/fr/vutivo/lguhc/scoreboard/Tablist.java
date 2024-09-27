package fr.vutivo.lguhc.scoreboard;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class TabList implements Listener {
    private final LGUHC main;
    private Joueur joueur;

    public TabList(LGUHC main) {
        this.main = main;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    set(player);
                }
            }
        }.runTaskTimer(main, 0L, 20L); // Met à jour la tablist toutes les 20 ticks (1 seconde)
    }

    public void set(Player player) {
        String gameInfo = "";

        if (main.PlayerHasRole) {
            if (main.hasRole(player)) {
                Joueur j = main.getPlayer(player.getUniqueId());

                gameInfo = "§b" + j.getRole().name + " §f--- §7" + j.getKills() + " kills";
            }
        }


        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText(main.TabName);
        Object footer = new ChatComponentText(main.TabTag+"\n"+ gameInfo + "\n" + "§bPlugin original par Lapin\n§dDéveloppé par Vutivo");
        try {
            Field a = packet.getClass().getDeclaredField("a");
            Field b = packet.getClass().getDeclaredField("b");
            a.setAccessible(true);
            b.setAccessible(true);

            a.set(packet, header);
            b.set(packet, footer);

            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

    }
}




