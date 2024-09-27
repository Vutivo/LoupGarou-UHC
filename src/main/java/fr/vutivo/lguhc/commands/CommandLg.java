package fr.vutivo.lguhc.commands;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.Joueur;
import fr.vutivo.lguhc.role.LgRoles;
import fr.vutivo.lguhc.role.use.Salvateur;
import fr.vutivo.lguhc.role.use.Sorciere;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;



public class CommandLg implements CommandExecutor {
    private final LGUHC main;


    public CommandLg(LGUHC main) {

        this.main = main;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {

            player.sendMessage(main.gameTag + "§3 Les commandes :");
            player.sendMessage("§9- /lg role /me : §6Infos sur le rôle");
            player.sendMessage("§9- /lg compo : §6Composition de la partie");
            player.sendMessage("§9- /lg doc : §6Accès au doc du mode de jeu");

            return false;
        }
        if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("me")) {
            if (args.length == 1) {
                if (main.getPlayer(player.getUniqueId()) == null) {
                    player.sendMessage(main.gameTag + " §cErreur, vous devez avoir un role");
                    return false;
                } else {
                    main.getPlayer(player.getUniqueId()).sendDesc();
                    return false;
                }
            }
        }
        if (args[0].equalsIgnoreCase("compo")) {
            if (!main.hideCompo) {
                if (main.HasRole(player.getUniqueId())) {

                    displayRoles(player);
                } else player.sendMessage(main.gameTag + "§cErreur, tu n'as pas encore de role");
            } else player.sendMessage(main.gameTag + "§cC'est une partie en compo cachée");

        }
        if (args[0].equalsIgnoreCase("proteger")) {

            if(args.length ==1){
                player.sendMessage(main.gameTag + "§bLa commande : /lg proteger <Joueur>");
                return false;
            }

            if (!canCommand(player)) return false;

            if (args.length > 2) {
                player.sendMessage(main.gameTag + "§bLa commande : /lg proteger <Joueur>");

                return false;
            }

            if (args.length == 2) {
                new Salvateur(main).canProtect(main.getPlayer(player.getUniqueId()), args[1]);
                return false;
            }
        }

        if(args[0].equalsIgnoreCase("revive")){
            if(args.length ==1){
                player.sendMessage(main.gameTag + "§bLa commande : /lg revive <Joueur>");
                return false;
            }

            if(args.length ==2){
                new Sorciere(main).Revive(main.getPlayer(player.getUniqueId()),args[1]);
                return false;
            }
        }

        return false;
    }

    private boolean canCommand(Player player) {

        if (main.getPlayer(player.getUniqueId()) == null) {
            player.sendMessage(main.gameTag + "§cVous devez avoir un rôle pour effectuer cette commande !");
            return false;
        } else {
            Joueur j = main.getPlayer(player.getUniqueId());
            if (j.isDead()) {
                player.sendMessage(main.gameTag + "§cVous devez être en vie pour effectuer cette commande !");
                return false;
            }

        }

        return true;
    }







    private void displayRoles(Player player) {
        int villageCount = 0;
        int lgCount = 0;
        int soloCount = 0;

        StringBuilder villageRoles = new StringBuilder();
        StringBuilder lgRoles = new StringBuilder();
        StringBuilder soloRoles = new StringBuilder();

        // Liste pour garder trace des rôles comptés
        Map<LgRoles, Integer> roleCounts = new HashMap<>();

        // Parcourir tous les joueurs pour compter les rôles actifs
        for (Joueur joueur : main.getJoueurs()) {
            LgRoles role = joueur.getRole();
            if (role != null) {
                roleCounts.put(role, roleCounts.getOrDefault(role, 0) + 1);
            }
        }

        // Afficher les rôles en jeu
        for (Map.Entry<LgRoles, Integer> entry : roleCounts.entrySet()) {
            LgRoles role = entry.getKey();
            int count = entry.getValue();

            String roleEntry = "§7- " + role.name + " : §e" + count + "\n";

            switch (role.camp) {
                case Village:
                    villageRoles.append("§a").append(roleEntry);
                    villageCount += count;
                    break;
                case LoupGarou:
                    lgRoles.append("§c").append(roleEntry);
                    lgCount += count;
                    break;
                case Assassin:
                    soloRoles.append("§6").append(roleEntry);
                    soloCount += count;
                    break;
            }
        }

        player.sendMessage("§aVillageois (" + villageCount + "):\n" + villageRoles);

        player.sendMessage("§cLoup-Garous (" + lgCount + "):\n" + lgRoles);

        player.sendMessage("§6Solos ("+soloCount +"):\n"+soloRoles);
    }




}






