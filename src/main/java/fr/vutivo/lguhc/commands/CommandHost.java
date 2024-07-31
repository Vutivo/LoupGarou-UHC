package fr.vutivo.lguhc.commands;

import fr.vutivo.lguhc.LGUHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHost implements CommandExecutor {
    private final LGUHC main;

    public CommandHost(LGUHC main) {
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
            player.sendMessage("§9- /host sethost <Joueur> §9[HOST]");
            player.sendMessage("§9- /host removehost <Joueur> §9[HOST]");
            player.sendMessage("§9- /host finalheal §9[HOST]");
            player.sendMessage("§9- /host giveall <objet> [nombre] §9[HOST]");

            return false;
        }

        if (args[0].equalsIgnoreCase("sethost")){
            if (args.length == 0){
                player.sendMessage("§9- /host sethost <Joueur> §9[HOST]");
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage("Ce joueur n'est pas en ligne ou n'existe pas !");
                return false;
            }

            Bukkit.broadcastMessage(main.gameTag + "§c" + targetPlayer.getName() + "§b est devenu le nouvelle host !");
            targetPlayer.addAttachment(main, "host", true);
            targetPlayer.recalculatePermissions();
            targetPlayer.setPlayerListName(main.PrefixTab + targetPlayer.getName()); // Change le nom dans la tablist

            ItemStack config = main.BuildItems(Material.COMMAND,0,"§4Config",null);
            targetPlayer.getInventory().setItem(2,config);
            return false;
        }

        if (args[0].equalsIgnoreCase("removehost")){
            if (args.length == 0){
                player.sendMessage("§9- /host removehost <Joueur> §9[HOST]");
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage("Ce joueur n'est pas en ligne ou n'existe pas !");
                return false;
            }

            Bukkit.broadcastMessage(main.gameTag + "§c" + targetPlayer.getName() + "§b n'est plus l'host de la partie !");
            targetPlayer.addAttachment(main, "host", false);
            targetPlayer.recalculatePermissions();
            targetPlayer.setPlayerListName(targetPlayer.getName()); // Change le nom dans la tablist

            targetPlayer.getInventory().clear();

            return false;
        }


        return false;


    }
}
