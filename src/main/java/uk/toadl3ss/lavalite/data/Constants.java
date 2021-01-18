package uk.toadl3ss.lavalite.data;

public class Constants {
    public static String GUILD_PREFIX;
    public static String ownerid;
    public static Boolean invite;
    public static String game;
    public static String status;
    public static void Init() {
        GUILD_PREFIX = Config.INS.getPrefix();
        ownerid = Config.INS.getOwnerID();
        invite = Config.INS.getInvite();
        game = Config.INS.getGame();
        status = Config.INS.getStatus();
    }
}