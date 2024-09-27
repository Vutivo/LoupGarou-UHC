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

    private boolean Dying = false;

    private boolean isRea = false;
    private boolean isInfect = false;

    private int Power;
    public boolean noPower = false;

    public boolean salvation = false;
    public Joueur whoProtect = null;
    private int kills;
    private boolean death = false;


    private LGUHC main;

    public Joueur(UUID uuid, LgRoles role, LgCamps camp, LGUHC main) {
        this.uuid = uuid;
        this.role = role;
        this.camp = camp;

        setName();
        Power();

        this.main = main;
    }

    public UUID getUUID() {
        return uuid;
    }


    public void setName() {
                                                         name = Bukkit.getPlayer(uuid).getName();
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

    public int getKills() {
        return kills;
    }
    public void addKills(){
        kills ++;
    }

    public LgCamps getCamp() {
        return camp;
    }
    public void sendDesc() {
        new LgDescription(this,main).sendDescription();
    }

    public void setPower(int power) {
        this.Power = power;
    }

    public int getPower() {
        return Power;
    }

    public boolean isRea(){
        return isRea;
    }
    public boolean isInfect(){
        return isInfect;
    }

    public void setRea(boolean rea) {
        isRea = rea;
    }

    public void setInfect(boolean infect) {
        isInfect = infect;
    }
    public boolean isConnected() {
        return getPlayer() != null;
    }

    public boolean isDead() {
        return death;
    }

    public void setDead(boolean death) {
        this.death= death;
    }

    public boolean isDying() {
        return Dying;
    }

    public void setDying(boolean Dying) {
       this.Dying =Dying;
    }




    private void Power(){
        if(role == LgRoles.ANCIEN) Power = 1;
        if(role == LgRoles.LGGrimeur) Power = 1;
        if(role ==LgRoles.IDV)Power = 1;
            if(role ==LgRoles.SOSO)Power = 1;
    }
}
