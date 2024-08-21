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


import java.util.HashMap;
import java.util.Map;

import static fr.vutivo.lguhc.LGUHC.BuildItems;


public class ConfigGui implements Listener {

    private final LGUHC main;


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

        if (event.getView().getTitle().equals(gameTag + "§6Configuration") ||
                event.getView().getTitle().equals(gameTag + "§6Roles") ||
                event.getView().getTitle().equals(gameTag + "§bTimer") ||
                event.getView().getTitle().equals(gameTag + "§bConfig UHC")){

            event.setCancelled(true);

            if (clickedItem == null || clickedItem.getType() == Material.AIR || meta == null || meta.getDisplayName() == null) {
                return;
            }

            switch (meta.getDisplayName()) {
                // Configuration
                case "§aStart":
                    start.runTaskTimer(main, 0, 20);
                    player.closeInventory();
                    break;

                case "§cStop":
                    if (main.start != null) {
                        main.start.StopStarting();
                        main.start = null;
                    }
                    break;

                case "§cscénario":
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

                //Config UHC
                case "§aBordure":
                    configTimer(player,"border");
                case "§cRoles":

                case "§bPvp":

                case "§einvincibilité":

                    break;




                // Roles
                case "§cLoup-Garou":
                    for (int i = 9; i <= 35; i++) {
                        event.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                    for (LgRoles role : LgRoles.values()) {
                        if (role.camp == LgCamps.LoupGarou) {
                            event.getInventory().setItem(slotRole, BuildItems(Material.BOOK, 1, 1, "§c" + role.name + " (" + role.number + ")"));
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
                            event.getInventory().setItem(slotRole, BuildItems(Material.BOOK, 1, 1, "§a" + role.name + " (" + role.number + ")"));
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
                            event.getInventory().setItem(slotRole, BuildItems(Material.BOOK, 1, 1, "§6" + role.name + " (" + role.number + ")"));
                            slotRole++;
                        }
                    }
                    break;
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
                ItemStack updatedItem = BuildItems(Material.BOOK, 1, 1, colorPrefix + selectedRole.name + " (" + selectedRole.number + ")");
                event.getInventory().setItem(event.getSlot(), updatedItem);
            }
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
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        Map<Integer, ItemStack> items = new HashMap<>();
        if (event.getView().getTitle().equals(gameTag + "§3Inv de départ")) {
            for (int i = 9; i < 36; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    items.put(i, item);
                }
            }
            main.StartInventory.put(player, items);

        }
    }


    private void Configuration(Player player) {

        Inventory config = Bukkit.createInventory(null, 27, gameTag + "§6Configuration");
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");

        ItemStack Start = BuildItems(Material.WOOL, 13, 1, "§aStart");
        ItemStack stopStart = BuildItems(Material.WOOL, 14, 1, "§cStop");

        ItemStack scenario = BuildItems(Material.ENDER_CHEST, 1, 1, "§cscénario");
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
        ItemStack Water = new ItemStack(Material.WATER_BUCKET, 1);
        ItemStack Food = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack Book = new ItemStack(Material.BOOK, 7);


        int[] glassSlots = {0, 1, 2, 3, 5, 6, 7, 8};
        for (int slotg : glassSlots) {
            ConfigInventory.setItem(slotg, glass);
        }
        ConfigInventory.setItem(4, Inventory);


        if (main.StartInventory.isEmpty()) {
            ConfigInventory.setItem(9, Food);
            ConfigInventory.setItem(10, Water);
            ConfigInventory.setItem(11, Book);
        }

        if (main.StartInventory.containsKey(player)) {
            Map<Integer, ItemStack> savedItems = main.StartInventory.get(player);
            for (Map.Entry<Integer, ItemStack> entry : savedItems.entrySet()) {
                ConfigInventory.setItem(entry.getKey(), entry.getValue());
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


        int[] glassSlots = {0, 2, 3, 5, 6, 8, 37, 38, 39, 40, 41, 42, 43};
        for (int slotg : glassSlots) {
            ConfigRole.setItem(slotg, glass);
        }
        ConfigRole.setItem(1, Village);
        ConfigRole.setItem(4, LG);
        ConfigRole.setItem(7, Solo);
        ConfigRole.setItem(36, Return);


        player.openInventory(ConfigRole);

    }

    private void Timer(Player player) {
        Inventory Timer = Bukkit.createInventory(null, 45,gameTag+ "§bConfig UHC");
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");
        ItemStack border = BuildItems(Material.BEACON,1,1,"§aBordure","§c" + main.Border);
        ItemStack maxborder = BuildItems(Material.BEACON,1,1,"§aBordure","§c" + main.maxBorder);
        ItemStack roles = BuildItems(Material.BEACON,1,1,"§cRoles");
        ItemStack pvp = BuildItems(Material.BEACON,1,1,"§bPvp");
        ItemStack invincibilite = BuildItems(Material.BEACON,1,1,"§einvincibilité");



        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 36, 36, 38, 43, 44};
        for (int slotg : glassSlots) {
            Timer.setItem(slotg, glass);
        }
        Timer.setItem(10,border);
        Timer.setItem(11,maxborder);
        Timer.setItem(16,roles);
        Timer.setItem(20,pvp);
        Timer.setItem(24,invincibilite);

        player.openInventory(Timer);

    }
    private void configTimer(Player player, String configType) {
        Inventory config = Bukkit.createInventory(null, 27, gameTag + "§bConfig UHC");
        ItemStack glass = BuildItems(Material.STAINED_GLASS_PANE, 1, 1, "§7");

        String[] names;
        int[] slots;

        switch (configType) {
            case "border":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 13, 14};
                break;
            case "roles":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 13, 14};
                break;
            case "pvp":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 13, 14};
                break;
            case "invincibility":
                names = new String[]{"§c-10", "§e-1", "§a+1", "§2+10"};
                slots = new int[]{11, 12, 13, 14};
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

        // Ajouter un item pour afficher la taille actuelle de la bordure
        ItemStack displayItem;
        switch (configType) {
            case "border":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aTaille actuelle de la bordure: " );
                break;
            case "roles":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration des rôles");
                break;
            case "pvp":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration PVP");
                break;
            case "invincibility":
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration Invincibilité");
                break;
            default:
                displayItem = BuildItems(Material.BOOKSHELF, 1, 1, "§aConfiguration");
                break;
        }
        config.setItem(22, displayItem); // Place it in the center

        int[] glassSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26};
        for (int slotg : glassSlots) {
            config.setItem(slotg, glass);
        }

        player.openInventory(config);
    }

}






