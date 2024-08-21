package fr.vutivo.lguhc.scoreboard;

import fr.vutivo.lguhc.LGUHC;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class TabList implements Listener {
    private LGUHC main;

    public  TabList(LGUHC main) {
        this.main = main;
    }


    public void set(Player player) {
        String gameInfo = "";
        String command = "";


        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText(main.TabName);
        Object footer = new ChatComponentText("\n"+main.TabTag+ gameInfo + "\n" + command + "\n\n§bPlugin original par Lapin\n§dDéveloppé par Vutivo");
        try {
            Field a = packet.getClass().getDeclaredField("a");
            Field b = packet.getClass().getDeclaredField("b");
            a.setAccessible(true);
            b.setAccessible(true);

            a.set(packet, header);
            b.set(packet, footer);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();


        }
    }
}




