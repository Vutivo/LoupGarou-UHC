package fr.vutivo.lguhc.role;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import org.bukkit.entity.Player;


public class LgDescription {


    private Joueur joueur;
    private Player player;

    private String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";

    public LgDescription(Joueur joueur,LGUHC main){
       this.joueur = joueur;
        this.player = joueur.getPlayer();
    }

    public void sendDescription(){

        StringBuilder roleName = new StringBuilder();
        roleName.append(gameTag+ "§9[Privé] Vous êtes §o§9" +joueur.getRole().name);

        roleName.append(". ");
        joueur.getPlayer().sendMessage(roleName.toString());

        LgRoles role = joueur.getRole();
        LgCamps camp = joueur.getCamp();

        StringBuilder desc = new StringBuilder();
        // camps des roles
        if(camp == LgCamps.Village) desc.append("§9 Vous gagnez avec le village. ");
        if(camp == LgCamps.LoupGarou) desc.append("§9 Vous gagnez avec les Loups Garoux. ");
        if(camp == LgCamps.Assassin) desc.append("§9 Vous devez gagner seul. ");

        desc.append("\n");


        // description des roles
        if (role == LgRoles.SV){
            desc.append("Vous êtes une sombre merde");
            joueur.getPlayer().sendMessage(desc.toString());
            return;
        }
        if (role == LgRoles.LG){

            desc.append("Vous avez très faim");
            joueur.getPlayer().sendMessage(desc.toString());
            return;
        }

        if (role == LgRoles.AS){

            desc.append("Vous êtes le GOAT");
            joueur.getPlayer().sendMessage(desc.toString());
            return;
        }

    }
}
