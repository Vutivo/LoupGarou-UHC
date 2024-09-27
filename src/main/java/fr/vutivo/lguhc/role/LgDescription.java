package fr.vutivo.lguhc.role;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class LgDescription {


    private Joueur joueur;
    private Player player;

    private final LGUHC main;

    private String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";

    public LgDescription(Joueur joueur,LGUHC main){
        this.main = main;
       this.joueur = joueur;
        this.player = joueur.getPlayer();
    }

    public void sendDescription(){

        joueur.getPlayer().sendMessage("§b==============================");
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

        //Vilage
        if (role == LgRoles.SV){
            desc.append("Vous avez aucun pouvoir en particulier.");
        }
        if(role == LgRoles.ANCIEN){
            String effect = "Vous obtenez l'effet resitance 1 de façon permanante.";
            String death ="Lorsque vous vous faite tué par la main d'un loup," +
                    "vous revenez à la vie.Cependant vous perdez votre effet de resistance.";

            desc.append(effect+"\n"+death);
        }
        if (role == LgRoles.SALVA){
            String cmd ="A chaque nouvel épisode, vous avez la possibilité de faire /lg protéger [pseudo]," +
                    "cela donnera l'effet resistance 1 ainsi que nofall jusqu'à la fin de l'épisode.";
            String item ="De plus, vous avez aussi 2 potions de soins";

            desc.append(cmd+"\n"+item);
        }
        if(role ==LgRoles.SOSO){
            String revive ="Vous avez la possibilité une fois dans la partie, de réssuciter un joueur qui" +
                    "vient de mourrir dans les 6 secondes qui suivent.";
            String item ="De plus vous avez à votre disposition 1 potion de soin, 2 potion de dégâts ainsi qu'une potion de régénération.";

            desc.append(revive +"\n"+item);
        }
        if(role == LgRoles.IDV){
            String revive ="Lorsque vous mourez, et que la personne qui vous à tué n'appartien pas au camp des loup-garou, tout le monde aura connaissance de votre role." +
                    "Cependant, vous perdez 2 coeur permanant jusqu'à la fin de la partie.";
            desc.append(revive);
        }

        //LG
         if (role == LgRoles.LG){
             String effect = "Vous obtenez l'effet force 1 de nuit.";
             String kill ="Lorsque vous tués quelqu'un vous obtenez l'effet speed 1 pendant une minute" +
                     " ainsi que 2 coeur d'absorption.";

            desc.append(effect+"\n"+kill);
        }
        if (role == LgRoles.LGGrimeur){
            String effect = "Vous obtenez l'effet force 1 de nuit.";
            String kill ="Lorsque vous tués quelqu'un vous obtenez l'effet speed 1 pendant une minute" +
                    " ainsi que 2 coeur d'absorption.";
            String grimeur ="Lorsque vous réaliserez votre premier kill, le role affiché du joueur sera Loup-Garou.";

            desc.append(effect+"\n"+grimeur+"\n"+kill);
        }

         //Solo
         if (role == LgRoles.AS){

             String item = "De plus vous disposez de 3 livre enchanté(Sharpness 3,Protection 3, Efficiency 3 & Unbreaking 3";
             String effect = "Vous obtenez l'effet force 1 de nuit.";

            desc.append(effect+"\n"+item);

        }

        joueur.getPlayer().sendMessage(desc.toString());

        joueur.getPlayer().sendMessage("§b==============================");

        if(main.Timer >= main.timelistlg && joueur.getCamp().equals(LgCamps.LoupGarou)){
            lgList(player);
        }


    }

    private void lgList(Player player) {
        List<String> lglist = new ArrayList<>();
        for (Joueur joueur : main.getJoueurs()) {
            if (joueur.getCamp().equals(LgCamps.LoupGarou)) {
                if (joueur.getPlayer() != null) {
                    lglist.add(joueur.getName());
                }
            }
        }

        if (!lglist.isEmpty()) {
            String message = "§cLoups-Garous: " + String.join(", ", lglist);
            player.sendMessage(message);
        }
    }
}
