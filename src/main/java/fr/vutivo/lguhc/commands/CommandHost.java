package fr.vutivo.lguhc.commands;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.gui.ConfigGui;
import fr.vutivo.lguhc.scoreboard.ScoreboardManager;
import fr.vutivo.lguhc.scoreboard.TabList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CommandHost implements CommandExecutor {
    private final LGUHC main;
    private String HOST_PREFIX ="§7[§6Host§7]§r ";

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

        if (!main.host.contains(player) && !player.isOp()) {
            player.sendMessage(main.gameTag + "§c Erreur tu n'es pas l'host de la partie");
            return false;
        }




        if(args.length == 0){

            player.sendMessage(main.gameTag + "§3 Les commandes :");
            player.sendMessage("§9- /host add|remove <Joueur> §9[HOST]");
            player.sendMessage("§9- /host config §9[HOST]");
            player.sendMessage("§9- /host say <Message> §9[HOST]");
            player.sendMessage("§9- /host reload §9[HOST]");
            player.sendMessage("§9- /host finalheal/fh §9[HOST]");
            player.sendMessage("§9- /host giveall <objet> [nombre] §9[HOST]");

            return false;
        }

        if (args[0].equalsIgnoreCase("add")){
            if (args.length == 1){
                player.sendMessage(main.gameTag +"§9- /host add <Joueur> §9[HOST]");
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(main.gameTag +"Ce joueur n'est pas en ligne ou n'existe pas !");
                return false;
            }
            if(main.host.contains(targetPlayer)){
                player.sendMessage(main.gameTag +"§c Le joueur est déja host");
                return false;
            }

            Bukkit.broadcastMessage(main.gameTag + "§c" + targetPlayer.getName() + "§b est devenu le nouvelle host !");
            main.host.add(targetPlayer);
            targetPlayer.setPlayerListName(main.PrefixTab + targetPlayer.getName()); // Change le nom dans la tablist

            if(main.isState(UHCState.WAITTING)|| main.isState(UHCState.STARTING)){
                ItemStack config = main.BuildItems(Material.COMMAND,0,1,"§4Config");
                targetPlayer.getInventory().setItem(4,config);
                return false;
            }


        }

        if (args[0].equalsIgnoreCase("remove")){
            if (args.length == 1){
                player.sendMessage(main.gameTag +"§9- /host remove <Joueur> §9[HOST]");
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(main.gameTag +"Ce joueur n'est pas en ligne ou n'existe pas !");
                return false;
            }
            if(!main.host.contains(targetPlayer)){
                player.sendMessage(main.gameTag +"§cLe joueur n'est pas l'host de la partie");
                return false;
            }

            Bukkit.broadcastMessage(main.gameTag + "§c" + targetPlayer.getName() + "§b n'est plus l'host de la partie !");
           main.host.remove(targetPlayer);
            targetPlayer.setPlayerListName(targetPlayer.getName()); // Change le nom dans la tablist

            if(main.isState(UHCState.WAITTING)|| main.isState(UHCState.STARTING)){
                targetPlayer.getInventory().clear();
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("finalheal")||args[0].equalsIgnoreCase("fh")){
            if(main.isState(UHCState.GAME)){
                for (Player players: Bukkit.getOnlinePlayers()){
                    players.setHealth(20);
                    players.sendMessage(main.gameTag +"§b Un final heal vient d'avoir lieu");
            }
                return false;

            }else {
                player.sendMessage(main.gameTag + "§c Erreur tu ne peux pas faire cette commande");
                return false;
            }
        }
        if (args[0].equalsIgnoreCase("config")){
            ConfigGui gui = new ConfigGui(main);
            gui.Configuration(player);
            return false;
        }
        if(args[0].equalsIgnoreCase("say")){
            String message = String.join(" ", Arrays.copyOfRange(args,1,args.length));

            String Bc = HOST_PREFIX + player.getName() + " : §b" + message;
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Bc);
            Bukkit.broadcastMessage("");
            return false;
        }
        if(args[0].equalsIgnoreCase("reload")){
            long startTime = System.currentTimeMillis(); // Commence le timer

            // Reload du plugin
            Bukkit.getPluginManager().disablePlugin(main);
            Bukkit.getPluginManager().enablePlugin(main);

            ScoreboardManager score = new ScoreboardManager(main);
            TabList tab = new TabList(main);

            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.teleport(main.Spawn);
                main.clearPlayer(pl);
                tab.set(pl);
                score.Setscoreboard(pl);

            }

            long endTime = System.currentTimeMillis(); // Fin du timer
            long elapsedTime = endTime - startTime; // Calcul du temps écoulé

            sender.sendMessage(main.gameTag + "§a Le plugin a été rechargé en " + elapsedTime + " ms.");
            return false;
        }



        return false;


    }
}
