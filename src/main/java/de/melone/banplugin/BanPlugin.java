package de.melone.banplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
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

public class BanPlugin {


    private final Logger logger;
    private final ProxyServer server;

    //NoSQL Ban
    public static String bansHost;
    public static int bansPort;
    public static String bansDatabase;
    public static String bansCollection;
    public static String bansUsername;
    public static String bansPassword;

    //NoSQL BanLog
    public static String banlogHost;
    public static int banlogPort;
    public static String banlogDatabase;
    public static String banlogCollection;
    public static String banlogUsername;
    public static String banlogPassword;

    //NoSQL BanIPs
    public static String banipHost;
    public static int banipPort;
    public static String banipDatabase;
    public static String banipCollection;
    public static String banipUsername;
    public static String banipPassword;

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

        Ban.ConnectionBan();
        Banlog.ConnectionBan();
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
                            Bans:
                              host: localhost
                              port: 27017
                              database: mydatabase
                              collection: myocllection
                              username: myuser
                              password: mypassword
                           
                            BanIP:
                              host: localhost
                              port: 27017
                              database: mydatabase
                              collection:
                              username: myuser
                              password: mypassword
                           
                            Banlog:
                              host: localhost
                              port: 27017
                              database: mydatabase
                              collection: myocllection
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
                                        Reason: Hacking
                                        time: 1
                                        type: Server
                                      2:
                                        Reason: AD
                                        time: 1
                                        type: Chat
                                      3:
                                        Reason: Spam
                                        time: 1
                                        type: Chat
                                      4:
                                        Reason: Insult
                                        time: 1
                                        type: Chat
                                      5:
                                        Reason: Bugusing
                                        time: 1
                                        type: Server
                                      6:
                                        Reason: Skin
                                        time: 1
                                        type: Server
                                      7:
                                        Reason: Hatespeech
                                        time: 1
                                        type: Chat
                                      8:
                                        Reason: Illegal buildings
                                        time: 1
                                        type: Server
                                      9:
                                        Reason: Beleidigung (Voice Mod)
                                        time: 1
                                        type: Server
                                      10:
                                        Reason: Soundbord (Voice Mod)
                                        time: 1
                                        type: Server
                                      11:
                                        Reason: Betteln
                                        time: 1
                                        type: Chat
                                      12:
                                         Reason: Ban Umgehung
                                        time: 1
                                        type: Server
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
                                    
                                    Prefix: <#ffa500>F<#f69d0e>u<#ec9507>c<#e38d01>h<#d98500>s<#cf7d00>c<#c67500>r<#bc6d00>a<#b36500>f<#a95d00>t<#9f5500>.<#954D00>d<#8B4500>e<gray>

                                    KickMessage: "%prefix% <newline> You have been warned/banned please Join New for more info"
                                    
                                    ReturnBan: "%prefix%  You have the player %targetPlayer% banned from the server because of %reson%"
                                    
                                    ReturnChatban: "%prefix% You have the Spieler %targetPlayer% banned from the Chat because of %reson%"
                                    
                                    Banlog1: "========== %prefix% =========="
                                    <newline>Points: %points%
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
                                    <newline> /unban <Player>
                                    NotFoundPlayerData: "No entry found for this player."
                                    PlayerNotFound: "Player Not Found"
                                    Playerunban: "%prefix% You have unban The player %player%
                                    NOSQLConnectionERROR: "%prefix% <newlien> Unfortunately we have made a mistake <newline> The conection to the database could not be established."
                                    NoPermission: %prefix% You have no Permissions to use this Command"""
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

            Map<String, Object> bans = (Map<String, Object>) data.get("Bans");
            Map<String, Object> banip = (Map<String, Object>) data.get("BanIP");
            Map<String, Object> banlog = (Map<String, Object>) data.get("Banlog");

            bansHost = (String) bans.get("host");
            bansPort = (Integer) bans.get("port");
            bansDatabase = (String) bans.get("database");
            bansCollection = (String) bans.get("collection");
            bansUsername = (String) bans.get("username");
            bansPassword = (String) bans.get("password");

            banipHost = (String) banip.get("host");
            banipPort = (Integer) banip.get("port");
            banipDatabase = (String) banip.get("database");
            banipCollection = (String) banip.get("collection");
            banipUsername = (String) banip.get("username");
            banipPassword = (String) banip.get("password");

            banlogHost = (String) banlog.get("host");
            banlogPort = (Integer) banlog.get("port");
            banlogDatabase = (String) banlog.get("database");
            banlogCollection = (String) banlog.get("collection");
            banlogUsername = (String) banlog.get("username");
            banlogPassword = (String) banlog.get("password");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
