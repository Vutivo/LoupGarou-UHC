package fr.vutivo.lguhc.role;

public enum LgRoles {

    //Loup Garou
    LG("Loup-garou", LgCamps.LoupGarou, 0),
    LGGrimeur("Loup-garou grimeur", LgCamps.LoupGarou, 0),
    //Villageois
    SV("Simple Villageois", LgCamps.Village, 0),
    ANCIEN("Ancien",LgCamps.Village,0),
    SALVA("Salvateur",LgCamps.Village,0),
    SOSO("Sorciere",LgCamps.Village,0),
    IDV("Idiot du village",LgCamps.Village,0),

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
