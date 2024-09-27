package fr.vutivo.lguhc.gui;

import fr.vutivo.lguhc.LGUHC;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.game.UHCStart;
import fr.vutivo.lguhc.role.LgCamps;
import fr.vutivo.lguhc.role.LgRoles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static fr.vutivo.lguhc.LGUHC.BuildItems;


public class ConfigGui implements Listener {

    private final LGUHC main;
    private String menu = "" ;
    private int number;


    public ConfigGui(LGUHC main) {
        this.main = main;
    }

    private final String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";




    @EventHandler
    public void OnInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack it = event.getItem();
        Action action = event.getAction();

        if (it == null) return;

        if (main.isState(UHCState.WAITTING) || main.isState(UHCState.STARTING)) {
            if (player.hasPermission("host")) {
                if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (it.isSimilar(BuildItems(Material.COMMAND, 0, 1, "§4Config"))) {
                        Configuration(player);

                    }
                }


            }
       }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        ItemMeta meta = clickedItem != null ? clickedItem.getItemMeta() : null;
        UHCStart start = new UHCStart(main);


        int slotRole = 9;
        String[] configType = {"Bordure","Bordure Max","Pvp", "Roles", "Reduction bordure","Invincibilite",
                                "Diamants max","Cycle","ListLg","§4Potion","Force","Resi"};

        if (clickedItem == null || clickedItem.getType() == Material.AIR || meta == null || meta.getDisplayName() == null) {
            return;
        }

        if (event.getView().getTitle().equals(gameTag + "§6Configuration") ||
                event.getView().getTitle().equals(gameTag + "§6Roles") ||
                event.getView().getTitle().equals(gameTag + "§bTimer") ||
                event.getView().getTitle().equals(gameTag + "§bConfig UHC")||
                Arrays.stream(configType).anyMatch(config -> event.getView().getTitle().equals(config))) {




            event.setCancelled(true);


            switch (meta.getDisplayName()) {
                // Configuration
                case "§4Retour":
                    Configuration(player);
                    break;
                case "§aStart":
                    start.runTaskTimer(main, 0, 20);
                    player.closeInventory();
                    break;

                case "§cStop":
                    if (main.start != null) {
                        main.start.StopStarting();
                        main.start = null;
                    }
                    player.closeInventory();
                    break;

                case "§cScénario":
                    player.sendMessage("En developpement");
                    break;
                case "§6Roles":
                    Roles(player);
                    break;
                case "§bConfig UHC":
                    Timer(player);
                    break;

                case "§3Inventaire":
                    StartInv(player);
                    break;

                // Roles
                case "§cLoup-Garou":
                    for (int i = 9; i <= 35; i++) {
                        event.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                    for (LgRoles role : LgRoles.values()) {
                        if (role.camp == LgCamps.LoupGarou) {
                            event.getInventory().setItem(slotRole, BuildItems(Material.BOOKSHELF, 1, 1, "§c" + role.name + " (" + role.number + ")"));
                            slotRole++;
                        }
                    }
                    break;
                case "§aVillageois":
                    for (int i = 9; i <= 35; i++) {
                        event.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                    for (LgRoles role : LgRoles.values()) {
                        if (role.camp == LgCamps.Village) {
                            event.getInventory().setItem(slotRole, BuildItems(Material.BOOKSHELF, 1, 1, "§a" + role.name + " (" + role.number + ")"));
                            slotRole++;
                        }
                    }
                    break;
                case "§6Solo":
                    for (int i = 9; i <= 35; i++) {
                        event.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                    for (LgRoles role : LgRoles.values()) {
                        if (role.camp == LgCamps.Assassin) {
                            event.getInventory().setItem(slotRole, BuildItems(Material.BOOKSHELF, 1, 1, "§6" + role.name + " (" + role.number + ")"));
                            slotRole++;
                        }
                    }
                    break;

                //Config UHC
                case "§3Compo cache":
                    if(main.hideCompo){
                        main.hideCompo = false;
                    }else {
                        main.hideCompo = true;
                    }
                    Timer(player);
                    break;
                case "§aBordure":
                    menu = "Bordure";
                    number= main.Border ;
                    configTimer(player,menu);
                    break;
                case "§8Bordure Max":
                    menu = "Bordure Max";
                    number = main.maxBorder;
                    configTimer(player,menu);
                    break;
                case "§6Reduction Bordure":
                    menu = "Reduction bordure";
                    number = main.reductionBorder;
                    configTimer(player,menu);

                    break;
                case "§cRoles":
                    menu = "Roles";
                    number = main.timerole;
                    configTimer(player,menu);
                    break;

                case "§bPvp":
                    menu = "Pvp";
                    number = main.timepvp;
                    configTimer(player,menu);
                    break;
                case "§eInvincibilite":
                    menu = "Invincibilite";
                    number = main.invincibility;
                    configTimer(player,menu);
                    break;

                case "§bDiamond":
                    menu = "Diamants max";
                    number = main.maxDiamond;
                    configTimer(player,menu);
                    break;

                case"§4List Lg":
                    menu = "ListLg";
                    number = main.timelistlg;
                    configTimer(player, menu);
                    break;


                case "§bCycle":
                    menu = "Cycle";
                    number = main.timeCycle;
                    configTimer(player,menu);
                    break;

                case "§4Potion":
                    PotionMenu(player);
                    break;

                //Potion
                case"§4Force":
                    menu = "Force";
                    number = main.strength;
                    configTimer(player,menu);
                    break;

                case"§8Resistance":
                    menu = "Resi";
                    number = main.resistance;
                    configTimer(player,menu);
                    break;
            }

            if(meta.getDisplayName().equals("§c-10")) {
                if(number >= 10){
                    number -= 10;
                    actualisation(menu);
                    configTimer(player,menu);
                }



            } else if (meta.getDisplayName().equals("§e-1")) {
                if(number >= 1){
                    number --;
                    actualisation(menu);
                    configTimer(player,menu);
                }


            } else if (meta.getDisplayName().equals("§a+1")) {
                number ++ ;
                actualisation(menu);
                configTimer(player,menu);



            } else if (meta.getDisplayName().equals("§2+10")) {
                number += 10;
                actualisation(menu);
                configTimer(player,menu);


            }




        }


        LgRoles selectedRole = null;
        if (meta != null && meta.getDisplayName() != null) {
            for (LgRoles role : LgRoles.values()) {
                String colorPrefix = getString(role);
                if (meta.getDisplayName().equals(colorPrefix + role.name + " (" + role.number + ")")) {
                    selectedRole = role;
                    break;
                }
            }

            if (selectedRole != null) {
                if (event.isLeftClick()) {
                    selectedRole.number++;
                    player.sendMessage("§aVous avez ajouté un " + selectedRole.name + ". Nombre actuel : " + selectedRole.number);
                    main.nbRoles++;
                } else if (event.isRightClick()) {
                    if (selectedRole.number > 0) {
                        selectedRole.number--;
                        player.sendMessage("§cVous avez retiré un " + selectedRole.name + ". Nombre actuel : " + selectedRole.number);
                        main.nbRoles--;
                    }
                }


                 String colorPrefix = getString(selectedRole);
                ItemStack updatedItem = BuildItems(Material.BOOKSHELF, 1, 1, colorPrefix + selectedRole.name + " (" + selectedRole.number + ")");
                event.getInventory().setItem(event.getSlot(), updatedItem);
            }
        }
    }

    private void PotionMenu(Player player) {
        Inventory potion = Bukkit.createInventory(null, 9,"§4Potion");

        ItemStack force = BuildItems(Material.BLAZE_POWDER,0,1,"§4Force","§c"+ main.strength+" %");
        ItemStack resi = BuildItems(Material.MAGMA_CREAM,0,1,"§8Resistance","§c"+ main.resistance+ " %");


        potion.setItem(3,force);
        potion.setItem(5,resi);

        player.openInventory(potion);

    }

    private void actualisation(String menu){
    switch (menu) {
        case "Bordure":
            main.Border = number;
            break;
        case "Bordure Max":
            main.maxBorder = number;
            break;
        case "Reduction bordure":
            main.reductionBorder = number;
            break;
        case "Roles":
            main.timerole = number;
            break;
        case "Pvp":
            main.timepvp = number;
            break;
        case "Diamants max":
            main.maxDiamond = number;
            break;
        case "Cycle":
            main.timeCycle = number;
            break;
        case "Force":
            main.strength = number;
            break;
        case "Resi":
            main.resistance = number;
            break;
        case "ListLg":
            main.timelistlg = number;
            break;
        case "Invincibilite":
            main.invincibility = number;
            break;
    }
}

    private String getString(LgRoles selectedRole) {
        String colorPrefix;
        if (selectedRole.camp == LgCamps.LoupGarou) {
            colorPrefix = "§c"; // Rouge pour les Loup-Garous
        } else if (selectedRole.camp == LgCamps.Village) {
            colorPrefix = "§a"; // Vert pour les Villageois
        } else if (selectedRole.camp == LgCamps.Assassin) {
            colorPrefix = "§6"; // Jaune/Or pour les Solitaires
        } else {
            colorPrefix = "§7"; // Gris par défaut pour tout autre camp
        }
        return colorPrefix;
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (event.getView().getTitle().equals(gameTag + "§3Inv de départ")) {
            for (int i = 9; i < 45; i++) {
                ItemStack item = inventory.getItem(i);
                main.statrtinginv.put(i,item);


                }
            }


        }



    public void Configuration(Player player) {

        Inventory config = Bukkit.createInventory(null, 27, gameTag + "§6Configuration");
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");

        ItemStack Start = BuildItems(Material.WOOL, 13, 1, "§aStart");
        ItemStack stopStart = BuildItems(Material.WOOL, 14, 1, "§cStop");

        ItemStack scenario = BuildItems(Material.ENDER_CHEST, 1, 1, "§cScénario");
        ItemStack LgConfig = BuildItems(Material.CHEST, 1, 1, "§6Roles");
        ItemStack Timer = BuildItems(Material.BOOKSHELF, 1, 1, "§bConfig UHC");
        ItemStack Inventaire = BuildItems(Material.CHEST, 1, 1, "§3Inventaire");

        //0  1  2  3  4  5  6  7  8
        //9  10 11 12 13 14 15 16 17
        //18 19 20 21 22 23 24 25 26
        //27 28 29 30 31 32 33 34 35
        //36 37 38 39 40 41 42 43 44

        int[] glassSlots = {0, 1, 7, 8, 9, 17, 18, 19, 25, 26};
        for (int slotg : glassSlots) {
            config.setItem(slotg, glass);
        }

        if (main.isState(UHCState.WAITTING)) {
            config.setItem(4, Start);

        }
        if (main.isState(UHCState.STARTING)) {
            config.setItem(4, stopStart);
        }

        config.setItem(10, scenario);
        config.setItem(11, LgConfig);
        config.setItem(12, Timer);
        config.setItem(16, Inventaire);


        player.openInventory(config);

    }


    private void StartInv(Player player) {

        Inventory ConfigInventory = Bukkit.createInventory(null, 45, gameTag + "§3Inv de départ");

        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");
        ItemStack Inventory = BuildItems(Material.CHEST, 1, 1, "§3Inventaire de départ");
        ItemStack steak = new ItemStack(Material.COOKED_BEEF,64);
        ItemStack water = new ItemStack(Material.WATER_BUCKET,1);
        ItemStack book = new ItemStack(Material.BOOK,11);



        int[] glassSlots = {0, 1, 2, 3, 5, 6, 7, 8};
        for (int slotg : glassSlots) {
            ConfigInventory.setItem(slotg, glass);
        }
        ConfigInventory.setItem(4, Inventory);

        if(main.statrtinginv.isEmpty()){
            ConfigInventory.setItem(9, steak);
            ConfigInventory.setItem(10, water);
            ConfigInventory.setItem(11, book);
        }else {
            // Parcours la HashMap et place les items dans l'inventaire du joueur
            for (Map.Entry<Integer, ItemStack> entry : main.statrtinginv.entrySet()) {
                int slot = entry.getKey();         // Le slot dans l'inventaire
                ItemStack item = entry.getValue(); // L'ItemStack à placer
                ConfigInventory.setItem(slot, item); // Place l'item dans le slot correspondant
            }

        }

        player.openInventory(ConfigInventory);
    }

    private void Roles(Player player) {

        Inventory ConfigRole = Bukkit.createInventory(null, 45, gameTag + "§6Roles");
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");
        ItemStack Village = BuildItems(Material.BOOKSHELF, 1, 1, "§aVillageois");
        ItemStack LG = BuildItems(Material.BOOKSHELF, 1, 1, "§cLoup-Garou");
        ItemStack Solo = BuildItems(Material.BOOKSHELF, 1, 1, "§6Solo");
        ItemStack Return = BuildItems(Material.BARRIER, 1, 1, "§4Retour");


        int[] glassSlots = {0, 2, 3, 5, 6, 8, 36,37, 38, 39, 40, 41, 42, 43};
        for (int slotg : glassSlots) {
            ConfigRole.setItem(slotg, glass);
        }
        ConfigRole.setItem(1, Village);
        ConfigRole.setItem(4, LG);
        ConfigRole.setItem(7, Solo);
        ConfigRole.setItem(44, Return);


        player.openInventory(ConfigRole);

    }

    private void Timer(Player player) {
        boolean hideCompo = main.hideCompo;
        String hideCompoText = hideCompo ? "§cOui" : "§aNon";

        Inventory Timer = Bukkit.createInventory(null, 45,gameTag+ "§bConfig UHC");
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");
        ItemStack border = BuildItems(Material.BEACON,1,1,"§aBordure","§c" + main.Border);
        ItemStack maxborder = BuildItems(Material.BEACON,1,1,"§8Bordure Max","§c" + main.maxBorder);
        ItemStack roles = BuildItems(Material.BOOKSHELF,1,1,"§cRoles","§c" + main.timerole+" minute(s)");
        ItemStack pvp = BuildItems(Material.DIAMOND_SWORD,0,1,"§bPvp","§c" + main.timepvp +" minute(s)");
        ItemStack invincibilite = BuildItems(Material.BEACON,1,1,"§eInvincibilite","§c" + main.invincibility+" minute(s)");
        ItemStack timeBorder = BuildItems(Material.BEACON,1,1,"§6Reduction Bordure","§c" + main.reductionBorder+" minute(s)");
        ItemStack diamondless = BuildItems(Material.DIAMOND,0,1,"§bDiamond","§c" + main.maxDiamond+" Diamant(s)");
        ItemStack Potion = BuildItems(Material.BLAZE_POWDER,0,1,"§4Potion","§c régler les % des effets de potion");
        ItemStack compo = BuildItems(Material.PAPER,0,1,"§3Compo cache",hideCompoText);
        ItemStack cycle = BuildItems(Material.WATCH,0,1,"§bCycle","§c"+main.timeCycle+" minute(s)");
        ItemStack listlg = BuildItems(Material.BOOK,0,1,"§4List Lg","§c"+ main.timelistlg+" minute(s)");

        ItemStack Return = BuildItems(Material.BARRIER, 1, 1, "§4Retour");



        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36,37,43 };
        for (int slotg : glassSlots) {
            Timer.setItem(slotg, glass);
        }
        Timer.setItem(4,compo);
        Timer.setItem(10,border);
        Timer.setItem(11,maxborder);
        Timer.setItem(16,roles);
        Timer.setItem(15,listlg);
        Timer.setItem(20,pvp);
        Timer.setItem(24,invincibilite);
        Timer.setItem(12, timeBorder);
        Timer.setItem(38, diamondless);
        Timer.setItem(28,Potion);
        Timer.setItem(3, cycle);

        Timer.setItem(44,Return);

        player.openInventory(Timer);

    }
    private void configTimer(Player player, String configType) {
        Inventory config = Bukkit.createInventory(null, 27, configType);
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");
        ItemStack Return = BuildItems(Material.BARRIER, 1, 1, "§4Retour");

        String[] names;
        int[] slots;

        switch (configType) {

            case "Bordure":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Bordure Max":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Roles":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Pvp":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Invincibilite":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Diamants max":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Reduction bordure":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Cycle":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "ListLg":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Force":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            case "Resi":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 14, 15};
                break;
            default:
                names = new String[0];
                slots = new int[0];
                break;
        }

        for (int i = 0; i < names.length; i++) {
            ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, 1);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(names[i]);
                item.setItemMeta(meta);
            }
            config.setItem(slots[i], item);
        }

        ItemStack displayItem;
        switch (configType) {
            case "Bordure":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aTaille actuelle de la bordure: ","§c" + main.Border );
                break;
            case "Bordure Max":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aTaille  de la bordure au maximum: ","§c" + main.maxBorder );
                break;
            case "Reduction bordure":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aTemps avant la réduction de la bordure","§c" + main.reductionBorder+" minute(s)");
                break;
            case "Roles":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration des rôles","§c" + main.timerole+" minute(s)");
                break;
            case "Pvp":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration PVP","§c" + main.timepvp+" minute(s)");
                break;
            case "Invincibilite":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration Invincibilité","§c" + main.invincibility+" minute(s)");
                break;
            case "Diamants max":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aDiamants maximum mine","§c" + main.maxDiamond+" Diamant(s)");
                break;
            case "Cycle":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aCycle Jour/Nuit ","§c" + main.timeCycle+" minute(s)");
                break;
            case "ListLg":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aListe des Loups-garoux","§c" + main.timelistlg+" minute(s)");
                break;
            case "Force":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aForce","§c" + main.strength+" %");
                break;
            case "Resi":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aResistance","§c" + main.resistance+" %");
                break;
            default:
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration");
                break;
        }
        config.setItem(22, displayItem);
        config.setItem(26,Return);



        int[] glassSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,13, 17, 18, 19, 20, 21, 23, 24, 25};
        for (int slotg : glassSlots) {
            config.setItem(slotg, glass);
        }


        player.openInventory(config);
    }

}






