package de.melone.banplugin;

import com.google.inject.Inject;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.cmd.CMD_ban;
import de.melone.banplugin.cmd.CMD_banlist;
import de.melone.banplugin.cmd.CMD_banlog;
import de.melone.banplugin.ulti.Ban;
import de.melone.banplugin.ulti.Banlog;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

@Plugin(id = "banplugin", name = "BanPlugin", version = "1.0-SNAPSHOT", authors = {"Melone145"})

public class BanPlugin {


    private final Logger logger;
    private final ProxyServer server;

    //NoSQL Ban
    public static String bansHost;
    public static int bansPort;
    public static String bansDatabase;
    public static String bansUsername;
    public static String bansPassword;

    //Ban reasons
    public static String reson1;
    public static String time1;
    public static String type1;

    public static String reson2;
    public static String time2;
    public static String type2;

    public static String reson3;
    public static String time3;
    public static String type3;

    public static String reson4;
    public static String time4;
    public static String type4;

    public static String reson5;
    public static String time5;
    public static String type5;

    public static String reson6;
    public static String time6;
    public static String type6;

    public static String reson7;
    public static String time7;
    public static String type7;

    public static String reson8;
    public static String time8;
    public static String type8;

    public static String reson9;
    public static String time9;
    public static String type9;

    public static String reson10;
    public static String time10;
    public static String type10;

    public static String reson11;
    public static String time11;
    public static String type11;

    public static String reson12;
    public static String time12;
    public static String type12;

    public static String MaxPoins;
    public static String Bantime;
    public static String MaxPoinsReason;

    //Read all messages
    public static String prefixMiniMessage;
    public static String KickMessage;
    public static String ReturnBan;
    public static String ReturnChatban;
    public static String Banlog1;
    public static String Banlog2;
    public static String NotFound;
    public static String BanHelp;
    public static String PlayerNotFound;
    public static String Playerunban;
    public static String ConnectionERROR;
    public static String noperms;
    public static String tempbanhelp;
    public static String errortempban;
    public static String banscreen;

    @Inject
    public BanPlugin(ProxyServer server, Logger logger, CommandManager commandManager, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.server = server;

        //Create Folder and All Files
        CreateFolder();
        CreateMongodb();
        CreateBanReasons();
        CreateMessage();

        readMongoDB("plugins/Bansystem/MongoDB.yml");
        readBanReasons("plugins/Bansystem/BanReasons.yml");
        readMessagesConfig("plugins/Bansystem/Messages.yml");

        if (testConnection()) {
            CollectionsCreate();
            Ban.ConnectionBan();
            Banlog.ConnectionBan();
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();
        commandManager.register("ban", new CMD_ban(server));
        commandManager.register("banlist", new CMD_banlist(server));
        commandManager.register("banlog", new CMD_banlog(server));
    }

    private void CreateFolder(){
        File folder = new File("plugins/Bansystem");

        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private void CreateMongodb() {
        File file = new File("plugins/Bansystem/MongoDB.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("""
                            Mongodb:
                              host: localhost
                              port: 27017
                              database: mydatabase
                              username: myuser
                              password: mypassword
                           \s""");
                } catch (IOException e) {
                    logger.error("Could not create config file", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void CreateBanReasons() {
        File banfile = new File("plugins/Bansystem/BanReasons.yml");

        if (!banfile.exists()) {
            try {
                banfile.createNewFile();
                try (FileWriter writer = new FileWriter(banfile)) {
                    writer.write(
                            """
                                    # All ban times are given in hours
                                    # Ban type is Chat and Server

                                    Bans:
                                        1:
                                          Reason: "Hacking"
                                          time: 1
                                          type: "Server"
                                         \s
                                        2:
                                          Reason: "AD"
                                          time: 1
                                          type: "Chat"
                                         \s
                                        3:
                                          Reason: "Spam"
                                          time: 1
                                          type: Chat
                                         \s
                                        4:
                                          Reason: Insult
                                          time: 1
                                          type: Chat
                                         \s
                                        5:
                                          Reason: Bugusing
                                          time: 1
                                          type: Server
                                         \s
                                        6:
                                          Reason: Skin
                                          time: 1
                                          type: Server
                                         \s
                                        7:
                                          Reason: Hatespeech
                                          time: 1
                                          type: Chat
                                         \s
                                        8:
                                          Reason: Illegal buildings
                                          time: 1
                                          type: Server
                                         \s
                                        9:
                                          Reason: Beleidigung (Voice Mod)
                                          time: 1
                                          type: Server
                                         \s
                                        10:
                                          Reason: Soundbord (Voice Mod)
                                          time: 1
                                          type: Server
                                         \s
                                        11:
                                          Reason: Betteln
                                          time: 1
                                          type: Chat
                                   \s
                                        12:
                                          Reason: Ban Umgehung
                                          time: 1
                                          type: Server
                                         \s
                                        Poins:
                                          Max Poins: 1
                                          Bantime: 1
                                          BanReason: Maximale Anzahl an Verfahrnungen"""
                    );
                } catch (IOException e) {
                    logger.error("Could not create config file", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void CreateMessage() {
        File messagesfile = new File("plugins/Bansystem/Messages.yml");

        if (!messagesfile.exists()) {
            try {
                messagesfile.createNewFile();
                try (FileWriter writer = new FileWriter(messagesfile)) {
                    writer.write(
                            """
                                    # Alle This Messages Support MIniMessages
                                    # https://docs.advntr.dev/index.html
                                   
                                    Prefix: "You Server"
                                    KickMessage: "%prefix% <newline> You have been warned/banned please Join New for more info"
                                   
                                    BanScreen: "%prefix% <newline>You are Banned <newline>Reason: %reason% <newline>until %time%<newline>You can make a unban application on unban.youserver.net"
                                   
                                    ReturnBan: "%prefix% You have the player %targetPlayer% banned from the server because of %reson%"
                                    
                                    ReturnChatban: "%prefix% You have the player %targetPlayer% banned from the Chat because of %reson%"
                                    
                                    tempbanhelp: "%prefix% Try /tempban <Player> <time in Hours> <type> <resion>"
                                    tempbanerror: "Please check your entry"
                                    
                                    Banlog1: "========== %prefix% ========= <
                                    newline>Points: %points%
                                    <newline>Number of bans: %logssize%
                                    <newline>last Ban:
                                    <newline>Reason: %grund%
                                    <newline>from: %fromplayer%
                                    <newline>Date: %date%"
                                    
                                    Banlog2: "========== %prefix% ==========
                                    <newline>Ban Nummer: %logIndex%
                                    <newline>Ban:
                                    <newline>Reason: %grund%
                                    <newline>from: %fromplayer%
                                    <newline>Date: %date%"
                                    
                                    BanHelp: "========== %prefix% ==========
                                    <newline> /ban <Player> 1,2,3...
                                    <newline /ban <Player> <time in Hours> <reson>
                                    <newline> /unban <Player>"
                                    
                                    NotFoundPlayerData: "No entry found for this player."
                                    
                                    PlayerNotFound: "Player Not Found"
                                    
                                    Playerunban: "%prefix% You have unban The player %player%"
                                    
                                    NOSQLConnectionERROR: "%prefix% <newlien> Unfortunately we have made a mistake <newline> The conection to the database could not be established."
                                    
                                    NoPermission: "%prefix% You have no Permissions to use this Command"
                                    """
                    );
                } catch (IOException e) {
                    logger.error("Could not create config file", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readMongoDB(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            Map<String, Object> data = yaml.load(inputStream);

            Map<String, Object> bans = (Map<String, Object>) data.get("Mongodb");

            bansHost = (String) bans.get("host");
            bansPort = (Integer) bans.get("port");
            bansDatabase = (String) bans.get("database");
            bansUsername = (String) bans.get("username");
            bansPassword = (String) bans.get("password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readBanReasons(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            Map<String, Object> data = yaml.load(inputStream);

            Map<String, Object> bans = (Map<String, Object>) data.get("Bans");

            Map<String, Object> ban1 = (Map<String, Object>) bans.get(1);
            Map<String, Object> ban2 = (Map<String, Object>) bans.get(2);
            Map<String, Object> ban3 = (Map<String, Object>) bans.get(3);
            Map<String, Object> ban4 = (Map<String, Object>) bans.get(4);
            Map<String, Object> ban5 = (Map<String, Object>) bans.get(5);
            Map<String, Object> ban6 = (Map<String, Object>) bans.get(6);
            Map<String, Object> ban7 = (Map<String, Object>) bans.get(7);
            Map<String, Object> ban8 = (Map<String, Object>) bans.get(8);
            Map<String, Object> ban9 = (Map<String, Object>) bans.get(9);
            Map<String, Object> ban10 = (Map<String, Object>) bans.get(10);
            Map<String, Object> ban11 = (Map<String, Object>) bans.get(11);
            Map<String, Object> ban12 = (Map<String, Object>) bans.get(12);
            Map<String, Object> poins = (Map<String, Object>) bans.get("Poins");

            // 1
            reson1 = (String) ban1.get("Reason");
            time1 = String.valueOf(ban1.get("time"));
            type1 = (String) ban1.get("type");

            // 2
            reson2 = (String) ban2.get("Reason");
            time2 = String.valueOf(ban2.get("time"));
            type2 = (String) ban2.get("type");

            // 3
            reson3 = (String) ban3.get("Reason");
            time3 = String.valueOf(ban3.get("time"));
            type3 = (String) ban3.get("type");

            // 4
            reson4 = (String) ban4.get("Reason");
            time4 = String.valueOf(ban4.get("time"));
            type4 = (String) ban4.get("type");

            // 5
            reson5 = (String) ban5.get("Reason");
            time5 = String.valueOf(ban5.get("time"));
            type5 = (String) ban5.get("type");

            // 6
            reson6 = (String) ban6.get("Reason");
            time6 = String.valueOf(ban6.get("time"));
            type6 = (String) ban6.get("type");

            // 7
            reson7 = (String) ban7.get("Reason");
            time7 = String.valueOf(ban7.get("time"));
            type7 = (String) ban7.get("type");

            // 8
            reson8 = (String) ban8.get("Reason");
            time8 = String.valueOf(ban8.get("time"));
            type8 = (String) ban8.get("type");

            reson9 = (String) ban9.get("Reason");
            time9 = String.valueOf(ban9.get("time"));
            type9 = (String) ban9.get("type");

            reson10 = (String) ban10.get("Reason");
            time10 = String.valueOf(ban10.get("time"));
            type10 = (String) ban10.get("type");

            reson11 = (String) ban11.get("Reason");
            time11 = String.valueOf(ban11.get("time"));
            type11 = (String) ban11.get("type");

            reson12 = (String) ban12.get("Reason");
            time12 = String.valueOf(ban12.get("time"));
            type12 = (String) ban12.get("type");

            //Poins
            MaxPoins = String.valueOf(poins.get("Max Poins"));
            Bantime = String.valueOf(poins.get("Bantime"));
            MaxPoinsReason = (String) poins.get("BanReason");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void readMessagesConfig(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            Map<String, Object> data = yaml.load(inputStream);

            prefixMiniMessage = (String) data.get("Prefix");
            KickMessage = (String) data.get("KickMessage");
            ReturnBan = (String) data.get("ReturnBan");
            ReturnChatban = (String) data.get("ReturnChatban");
            Banlog1 = (String) data.get("Banlog1");
            Banlog2 = (String) data.get("Banlog2");
            NotFound = (String) data.get("NotFoundPlayerData");
            BanHelp = (String) data.get("BanHelp");
            PlayerNotFound = (String) data.get("PlayerNotFound");
            Playerunban = (String) data.get("Playerunban");
            ConnectionERROR = (String) data.get("NOSQLConnectionERROR");
            noperms = (String) data.get("NoPermission");
            tempbanhelp = (String) data.get("tempbanhelp");
            errortempban = (String) data.get("tempbanerror");
            banscreen = (String) data.get("BanScreen");

            System.out.println(data.keySet()); // Gibt alle verfügbaren Schlüssel aus

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    public void readMessagesConfig(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            Map<String, String> data = yaml.load(inputStream);

            prefixMiniMessage = data.get("Prefix");
            KickMessage = data.get("KickMessage");
            ReturnBan = data.get("ReturnBan");
            ReturnChatban = data.get("ReturnChatban");
            Banlog1 = data.get("Banlog1");
            Banlog2 = data.get("Banlog2");
            NotFound =  data.get("NotFoundPlayerData");
            BanHelp = data.get("BanHelp");
            PlayerNotFound = data.get("PlayerNotFound");
            Playerunban =  data.get("Playerunban");
            ConnectionERROR = data.get("NOSQLConnectionERROR");
            noperms = data.get("NoPermission");
            tempbanhelp = data.get("tempbanhelp");
            errortempban = data.get("tempbanerror");
            banscreen = data.get("BanScreen");

            System.out.println(data.keySet()); // Gibt alle verfügbaren Schlüssel aus

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void CollectionsCreate(){
        try (MongoClient mongoClient = MongoClients.create("mongodb://" + bansUsername + ":" + bansPassword + "@" + bansHost + ":" + bansPort + "/?authSource=" + bansDatabase + "&authMechanism=SCRAM-SHA-1")) {
            MongoDatabase database = mongoClient.getDatabase("Bans");

            if (collectionExists(database, "Bans")) {
                database.createCollection("Bans");
                return;
            }

            if (collectionExists(database, "Banlog")){
                database.createCollection("Banlog");
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    private static boolean collectionExists(MongoDatabase database, String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                return false;
            }
        }
        return true;
    }

    public static boolean testConnection() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://" + BanPlugin.bansUsername + ":" + BanPlugin.bansPassword + "@" + BanPlugin.bansHost + ":" + BanPlugin.bansPort + "/?authSource=" + BanPlugin.bansDatabase + "&authMechanism=SCRAM-SHA-1")) {
            MongoDatabase database = mongoClient.getDatabase(bansDatabase);

            database.listCollectionNames().first();
            return true;

        } catch (MongoTimeoutException e) {
            System.err.println("Connection timeout");
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return false;
    }
}
