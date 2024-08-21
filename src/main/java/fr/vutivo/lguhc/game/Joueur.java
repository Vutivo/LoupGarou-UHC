package fr.vutivo.lguhc.game;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.role.LgCamps;
import fr.vutivo.lguhc.role.LgDescription;
import fr.vutivo.lguhc.role.LgRoles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Joueur {

    private UUID uuid;
    private String name;
    private LgRoles role;
    private LgCamps camp;
    private boolean death = false;


    private LGUHC main;

    public Joueur(UUID uuid, LgRoles role, LgCamps camp, LGUHC main) {
        this.uuid = uuid;
        this.role = role;
        this.camp = camp;

        this.main = main;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName(){
        return name;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public LgRoles getRole() {
        return role;
    }


    public LgCamps getCamp() {
        return camp;
    }
    public void sendDesc() {
        new LgDescription(this,main).sendDescription();
    }


    public boolean isConnected() {
        return getPlayer() != null;
    }

    public boolean isDead() {
        return death;
    }
}
