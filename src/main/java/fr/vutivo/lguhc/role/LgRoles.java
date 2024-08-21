package fr.vutivo.lguhc.role;

public enum LgRoles {

    //Loup Garou
    LG("Loup-garou", LgCamps.LoupGarou, 0),
    LGG("Loup-garou grimeur", LgCamps.LoupGarou, 0),
    //Villageois
    SV("Simple Villageois", LgCamps.Village, 0),
    //Solo
    AS("Assassin" , LgCamps.Assassin ,0);

    public String name;
    public LgCamps camp;
    public int number;

    LgRoles(String name, LgCamps camp, int number) {
        this.name = name;
        this.camp = camp;
        this.number = number;

    }
}
