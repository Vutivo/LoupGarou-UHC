package fr.vutivo.lguhc.role.use;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import fr.vutivo.lguhc.role.LgRoles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Salvateur {
    private LGUHC main;
    public Salvateur(LGUHC main) {
        this.main = main;
    }

    public void canProtect(){

        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run() {
                cannotProtect();
            }
        },2400);

        for(Joueur joueur : main.getJoueurs()){
            if(joueur.getRole().equals(LgRoles.SALVA)){
                if(!joueur.isDead()){
                    joueur.setPower(1);

                    if(joueur.isConnected()){
                        joueur.getPlayer().sendMessage(main.gameTag+"§b Vous avez 2 minute pour proteger un joueur grace à la comande /lg proteger [pseudo]");
                    }
                }
            }
        }

    }

    private void cannotProtect(){
        for(Joueur joueur : main.getJoueurs()){
            if(joueur.getRole()== LgRoles.SALVA){
                if(!joueur.isDead()){
                    if(joueur.getPower() > 0){
                        joueur.setPower(0);
                        joueur.whoProtect = null;

                        if(joueur.isConnected()){
                            joueur.getPlayer().sendMessage(main.gameTag+"§cVous avez attendu plus de 2minutes. Vous pourrez donc utiliser votre pouvoir au prochaine épisode.");
                        }

                    }
                }

            }
        }
    }


    public void canProtect(Joueur joueur,String targetname){

        if(!joueur.getRole().equals(LgRoles.SALVA)){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur vous devez être §asalvateur §cpour effectuer cette commande !");
            return;
        }
        if(joueur.getPower() < 1){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur vous avez deja utilisé se pouvoir ou attendu trop longtemps !");
            return;
        }
        if(Bukkit.getPlayer(targetname)== null){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur, le joueur "+targetname+" n'est pas connecter ou n'existe pas!");
        }
        Player Target = Bukkit.getPlayer(targetname);


        if(main.getPlayer(Target.getUniqueId()) ==null){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur le joueur visé dois être dans la partie!");
            return;
        }

        Joueur TargetJ = main.getPlayer(Target.getUniqueId());

        if(TargetJ.isDead()){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur le joueur dois être en vie!");
            return;
        }
        if(joueur.whoProtect != null) {
            if (TargetJ.equals(joueur.whoProtect)) {
                joueur.getPlayer().sendMessage(main.gameTag + "§c Erreur vous ne pouvez pas mettre la salation à la même personne que l'épisode dernier!");
            }
        }

        protect(joueur,TargetJ);


    }
    private void protect (Joueur j,Joueur t){
        j.getPlayer().sendMessage(main.gameTag+"§b vous venez de donner la salvation à "+t.getName());
        j.setPower(0);
        j.whoProtect = t;
        t.salvation =true;
        t.getPlayer().sendMessage(main.gameTag+"§b Vous venez de recevoir la salvation");
    }

}
