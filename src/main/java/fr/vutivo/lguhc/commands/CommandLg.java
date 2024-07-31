package fr.vutivo.lguhc.commands;

import fr.vutivo.lguhc.LGUHC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLg implements CommandExecutor {
    private final LGUHC main;

    public CommandLg(LGUHC main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)){
            System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
            return false;
        }
        Player player = (Player) sender;

        if(args.length == 0){

            player.sendMessage(main.gameTag + "§3 Les commandes :");
            player.sendMessage("§9- /lg role : §6Infos sur le rôle");
            player.sendMessage("§9- /lg compo : §6Composition de la partie");
            player.sendMessage("§9- /lg doc : §6Accès au doc du mode de jeu");
        }

        return false;
    }
}
