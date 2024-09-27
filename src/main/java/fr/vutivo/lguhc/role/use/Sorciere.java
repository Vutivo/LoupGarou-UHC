package fr.vutivo.lguhc.role.use;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import fr.vutivo.lguhc.role.LgRoles;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;



public class Sorciere {
    private LGUHC main;
    public Sorciere(LGUHC main) {
        this.main = main;
    }

    public void Revive(Joueur joueur, String targetname){
        if(!joueur.getRole().equals(LgRoles.SOSO)){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur vous devez être sorcière pour effectuer cette action !");
            return;

        }
        if(joueur.getPower() < 1){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur vous avez deja utilisé se pouvoir !");
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

        if(TargetJ.isRea()){
            joueur.getPlayer().sendMessage(main.gameTag+"§cErreur le joueur à déja été réanimé.");
            return;
        }
        if(TargetJ.equals(joueur)){
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur vous ne pouvez pas vour réanimer vous même.");
            return;
        }

        if(TargetJ.isDying()){
            TargetJ.setRea(true);
            TargetJ.setDying(false);
            joueur.setPower(0);
            joueur.getPlayer().sendMessage(main.gameTag+"§b Vous venez de sauvé "+targetname+".");
            TargetJ.getPlayer().sendMessage(main.gameTag+"§b Vous venez de vous faire séanimer par la sorcière.");
            TargetJ.getPlayer().setGameMode(GameMode.SURVIVAL);
            main.randomTp(TargetJ.getPlayer());

        }else{
            joueur.getPlayer().sendMessage(main.gameTag+"§c Erreur le joueur ne peut pas être réanimé.");
        }


        }


        public void ReaMsg(Player victim){

            for (Joueur j : main.getJoueurs()) {
                if (j.getPower() > 0 ) {
                    if(j.getRole() == LgRoles.SOSO){
                        if (!j.getPlayer().equals(victim)) {
                            TextComponent msg = new TextComponent(main.gameTag + "§6Le joueur §f" + victim.getName() + " §6 est Mort ! Vous avez 6 secondes pour le réanimer en cliquant "
                                    + "sur le message !");

                            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Réanimer ?").create()));
                            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg revive " + victim.getName()));
                            j.getPlayer().spigot().sendMessage(msg);
                            j.getPlayer().sendMessage("");
                        }
                    }

                }
            }
        }

        }



