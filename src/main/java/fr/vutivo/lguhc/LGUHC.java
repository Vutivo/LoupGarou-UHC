package fr.vutivo.lguhc;

import fr.vutivo.lguhc.commands.CommandHost;
import fr.vutivo.lguhc.commands.CommandLg;
import fr.vutivo.lguhc.event.EventManager;
import fr.vutivo.lguhc.game.Joueur;
import fr.vutivo.lguhc.game.UHCState;
import fr.vutivo.lguhc.role.LgRoles;
import fr.vutivo.lguhc.game.UHCStart;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.*;


public final class LGUHC extends JavaPlugin {


    public List<UUID> PlayerInGame = new ArrayList<>();
    public List<Joueur> Players = new ArrayList<>();
    public ArrayList<LgRoles> compo = new ArrayList<>();
    public ArrayList<Player> host = new ArrayList<>();
    public ArrayList<Player> damageFall = new ArrayList<>();


    public HashMap <Integer,ItemStack> statrtinginv = new HashMap<>();


    public  String TabName = "§5§k|||§6 Vuti§bGAMES §5§k|||";
    public String TabTag = "§6§lLOUP-GAROU§r";
    public String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";
    public String PrefixTab = "§c HOST §r";

    public UHCStart start;
    public UHCState state;


    public boolean hideCompo = false;

    public String Cycle ="Jour";
    public int Timer = 0;
    public int timerole = 2;
    public int timepvp = 30;
    public boolean ispvp = false ;
    public int invincibility = 1;
    public boolean isinvincibility = true;
    public int maxDiamond = 17;
    public int timelistlg = 35;
    public int episode = 1;
    public int epTime = 20;
    public int timeCycle = 5;
    public int grouplist = 5;
    public Integer playerIG = 0;
    public Integer nbRoles = 0;
    public boolean PlayerHasRole = false;

    //Cage
    public boolean cage;
    public Integer cageSize;
    public Integer cageHeight;

    public World world;
    public WorldBorder wb;
    public int Border = 1000;
    public int maxBorder = 300;
    public int reductionBorder = 90;
    public int strength = 50;
    public int resistance = 20;
    public Location Spawn;
    public Integer MaxY = 150;
    public Integer MaxRange = 0;



    //Color
    public final String RESET = "\u001B[0m";
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String BLUE = "\u001B[34m";
    public final String CYAN = "\u001B[36m";



    @Override
    public void onEnable() {
        System.out.println(GREEN + "[LOUP-GAROUS] Le plugin est en cours de demarage");

        //Save du config.yml
        saveDefaultConfig();

        loadConfig();

        

        if (world != null) {
            world.setPVP(true);
            world.setGameRuleValue("reducedDebugInfo", "true");
            world.setGameRuleValue("naturalRegeneration", "false");
            world.setGameRuleValue("announceAdvancements", "false");
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("showDeathMessages", "false");
            world.setStorm(false);
            world.setThundering(false);
            world.setDifficulty(Difficulty.NORMAL);
            wb = world.getWorldBorder();

        } else {
            getLogger().severe(RED + "LOUP-GAROUS Impossible de trouver le monde !");
        }
        EventManager.registerEvents(this);
        state = UHCState.WAITTING;
        playerIG = Bukkit.getOnlinePlayers().size();



        getCommand("lg").setExecutor(new CommandLg(this));
        getCommand("host").setExecutor(new CommandHost(this));




        System.out.println(RED +  "========================================" + RESET);
        System.out.println(GREEN + "Plugin LOUP-GAROU UHC - Version 0.1" + RESET);
        System.out.println(YELLOW + "       Auteur : Vutivo" + RESET);
        System.out.println(RED +  "========================================" + RESET);

    }

    @Override
    public void onDisable() {
        deleteCage();
        System.out.println(BLUE + "[LOUP-GAROUS] Le plugin s'eteint");
    }


    private void loadConfig() {

        world = Bukkit.getWorld(getConfig().getString("Spawn.worldName"));
        Spawn = getLocation(getConfig().getString("Spawn.location"));

        cage = getConfig().getBoolean("Cage.state");
        cageSize = getConfig().getInt("Cage.cageSize");
        cageHeight = getConfig().getInt("Cage.cageHeight");

        MaxY = getConfig().getInt("Spawn.maxY");
        MaxRange = getConfig().getInt("Spawn.maxRange");



        if (!cage) {
            System.out.println(RED + "LOUP-GAROUS La cage n'a pas ete build");
        } else {
            createCage();
        }


    }


    //STATE
    public void setState(UHCState state) {
        this.state = state;
    }


    public Boolean isState(UHCState state) {
        return this.state == state;
    }



    public void createCage() {
        World world = Bukkit.getWorlds().get(0);
        int startY = 150;

//Plateform
        for (int x = -cageSize / 2; x < cageSize / 2; x++) {
            for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                Block block = world.getBlockAt(x, startY, z);
                block.setType(Material.BARRIER);

            }
        }
//Wall
        for (int y = startY + 1; y <= startY + cageHeight; y++) {
            for (int x = -cageSize / 2; x < cageSize / 2; x++) {
                for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                    if (x == -cageSize / 2 || x == cageSize / 2 - 1 || z == -cageSize / 2 || z == cageSize / 2 - 1) {
                        Block block = world.getBlockAt(x, y, z);
                        block.setType(Material.BARRIER);
                    }
                }
            }
        }

        System.out.println(CYAN + "[LOUP-GAROUS] La cage au spawn a ete genere !");
    }

    public void deleteCage() {
        World world = Bukkit.getWorlds().get(0);
        int startY = 150;

//Plateform
        for (int x = -cageSize / 2; x < cageSize / 2; x++) {
            for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                Block block = world.getBlockAt(x, startY, z);
                block.setType(Material.AIR);

            }
        }
//Wall
        for (int y = startY + 1; y <= startY + cageHeight; y++) {
            for (int x = -cageSize / 2; x < cageSize / 2; x++) {
                for (int z = -cageSize / 2; z < cageSize / 2; z++) {
                    if (x == -cageSize / 2 || x == cageSize / 2 - 1 || z == -cageSize / 2 || z == cageSize / 2 - 1) {
                        Block block = world.getBlockAt(x, y, z);
                        block.setType(Material.AIR);
                    }
                }
            }
        }


    }
    private Location getLocation(String loc) {
        String[] args = loc.split(",");

        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);

        return new Location(world, x, y, z);
    }

    public void randomTp(Player player) {

        Random random = new Random();

        int rangeMax = (int) Border /2 - 15;
        int rangeMin = (int) -Border /2 + 15;

        if (MaxRange != 0) {
            rangeMax = (int) MaxRange - 15;
            rangeMin = - (int) MaxRange + 15;
        }

        int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int y = MaxY;

        Location teleportLocation = new Location(player.getWorld(), x + Spawn.getX(), y, z + Spawn.getZ());

        player.teleport(teleportLocation);
        damageFall.add(player);
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                damageFall.remove(player);
            }
        }, 100L);



    }



    public  static ItemStack BuildItems(Material material,int color,int amount, String name){
        ItemStack item = new ItemStack(material , amount, (short) color); // si pas de couleur le mettre à 0
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;

    }


    public  static ItemStack BuildItems(Material material,int color,int amount, String name,String lore){
        ItemStack item = new ItemStack(material , amount, (short) color); // si pas de couleur le mettre à 0
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Collections.singletonList(lore));
        meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;

    }


    public Joueur getPlayer(UUID uuid){
        for (Joueur player : Players){
            if (player.getUUID().equals(uuid)){
                return player;
            }
        }
        return null;
    }



    public List<Joueur> getJoueurs() {
        List<Joueur> joueurs = new ArrayList<>();
        for (Joueur joueur : Players) {
            if (joueur.isConnected()) {
                if (!joueur.isDead()) {
                    joueurs.add(joueur);
                }
            }
        }

        return joueurs;
    }



    public boolean HasRole(UUID uuid){
        if(getPlayer(uuid) == null)return false;
        else return true;
    }


    public boolean hasRole(UUID uuid) {
    if (getPlayer(uuid)== null) {
        return false;
    } else return true;

    }

    public boolean hasRole(Player player) {
        return hasRole(player.getUniqueId());

    }

    public void clearPlayer(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.updateInventory();
        player.setExp(0);
        player.setLevel(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

    }



}

