package de.melone.banplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.Listener.JoinEvent;
import de.melone.banplugin.Listener.PlayerChat;
import de.melone.banplugin.cmd.CMD_ban;
import de.melone.banplugin.cmd.CMD_banlog;
import de.melone.banplugin.cmd.CMD_unban;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.BanlogSQL;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Plugin(
        id = "ban_plugin",
        name = "Ban Plugin",
        version = "1.0-SNAPSHOT"
)
public class BanPlugin {

    public static ArrayList<String> playerChatAllow = new ArrayList<>();

    private final Logger logger;
    private final ProxyServer server;
    private final CommandManager commandManager;

    public static String bansHost;
    public static int bansPort;
    public static String bansDatabase;
    public static String bansCollection;
    public static String bansUsername;
    public static String bansPassword;

    public static String banlogHost;
    public static int banlogPort;
    public static String banlogDatabase;
    public static String banlogCollection;
    public static String banlogUsername;
    public static String banlogPassword;

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

    public static String MaxPoins;
    public static String Bantime;
    public static String MaxPoinsReason;

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

    @DataDirectory
    private final Path dataDirectory;

    @Inject
    public BanPlugin(ProxyServer server, Logger logger, CommandManager commandManager, @DataDirectory Path dataDirectory) throws FileNotFoundException {
        this.server = server;
        this.logger = logger;
        this.commandManager = commandManager;
        this.dataDirectory = dataDirectory;

        createConfig();
        readBanConfig("plugins/Bansystem/MongoDB.yml");
        readSettingsConfig("plugins/Bansystem/Ban.yml");
        readMessagesConfig("plugins/Bansystem/Messages.yml");

        BanSQL.ConnectionBan();
        BanlogSQL.ConnectionBan();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new JoinEvent());
        server.getEventManager().register(this, new PlayerChat());

        CommandManager commandManager = server.getCommandManager();
        commandManager.register("ban", new CMD_ban(server));
        commandManager.register("unban", new CMD_unban(server));
        commandManager.register("banlog", new CMD_banlog(server));
    }

    private void createConfig() {
        File folder = new File("plugins/Bansystem");
        File file = new File("plugins/Bansystem/MongoDB.yml");
        File banfile = new File("plugins/Bansystem/Ban.yml");
        File messagesfile = new File("plugins/Bansystem/Messages.yml");

        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Bans:\n" +
                            "  host: localhost\n" +
                            "  port: 27017\n" +
                            "  database: mydatabase\n" +
                            "  collection: myocllection\n" +
                            "  username: myuser\n" +
                            "  password: mypassword\n" +
                            "Banlog:\n" +
                            "  host: localhost\n" +
                            "  port: 27017\n" +
                            "  database: mydatabase\n" +
                            "  collection: myocllection\n" +
                            "  username: myuser\n" +
                            "  password: mypassword\n");
                } catch (IOException e) {
                    logger.error("Could not create config file", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!banfile.exists()) {
            try {
                banfile.createNewFile();
                try (FileWriter writer = new FileWriter(banfile)) {
                    writer.write(
                            "# All ban times are given in hours\n" +
                                    "# Ban type is Chat and Server\n" +
                                    "\n" +
                                    "Bans:\n" +
                                    "  1:\n" +
                                    "    Reason: Hacking\n" +
                                    "    time: 1 \n" +
                                    "    type: Server\n" +
                                    "  2:\n" +
                                    "    Reason: AD\n" +
                                    "    time: 1\n" +
                                    "    type: Chat\n" +
                                    "  3:\n" +
                                    "    Reason: Spam\n" +
                                    "    time: 1\n" +
                                    "    type: Chat\n" +
                                    "  4:\n" +
                                    "    Reason: Insult\n" +
                                    "    time: 1\n" +
                                    "    type: Chat\n" +
                                    "  5:\n" +
                                    "    Reason: Bugusing\n" +
                                    "    time: 1\n" +
                                    "    type: Server\n" +
                                    "  6:\n" +
                                    "    Reason: Skin\n" +
                                    "    time: 1\n" +
                                    "    type: Server\n" +
                                    "  7:\n" +
                                    "    Reason: Hatespeech\n" +
                                    "    time: 1\n" +
                                    "    type: Chat\n" +
                                    "  8:\n" +
                                    "    Reason: Illegal buildings\n" +
                                    "    time: 1\n" +
                                    "    type: Server\n" +
                                    "  Poins:\n" +
                                    "    Max Poins: 1\n" +
                                    "    Bantime: 1\n" +
                                    "    BanReason: Maximale Anzahl an Verfahrnungen"
                    );
                } catch (IOException e) {
                    logger.error("Could not create config file", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!messagesfile.exists()) {
            try {
                messagesfile.createNewFile();
                try (FileWriter writer = new FileWriter(messagesfile)) {
                    writer.write(
                            "# Alle This Messages Support MIniMessages \n" +
                            "# https://docs.advntr.dev/index.html \n \n" +

                            "Prefix: <#ffa500>F<#f69d0e>u<#ec9507>c<#e38d01>h<#d98500>s<#cf7d00>c<#c67500>r<#bc6d00>a<#b36500>f<#a95d00>t<#9f5500>.<#954D00>d<#8B4500>e<gray> \n\n" +

                            "KickMessage: \"%prefix% <newline> You have been warned/banned please Join New for more info\" \n \n" +

                            "ReturnBan: \"%prefix%  You have the player %targetPlayer% banned from the server because of %reson%\" \n \n" +

                            "ReturnChatban: \"%prefix% You have the Spieler %targetPlayer% banned from the Chat because of %reson%\" \n \n" +

                            "Banlog1: \"========== %prefix% ========== " +
                            "<newline>Points: %points% \n" +
                            "<newline>Number of bans: %logssize% \n" +
                            "<newline>last Ban: \n" +
                            "<newline>Reason: %grund% \n" +
                            "<newline>from: %fromplayer% \n" +
                            "<newline>Date: %date%\" \n \n" +

                            "Banlog2: \"========== %prefix% ==========\n" +
                            "<newline>Ban Nummer: %logIndex% \n" +
                            "<newline>Ban: \n" +
                            "<newline>Reason: %grund% \n" +
                            "<newline>from: %fromplayer% \n" +
                            "<newline>Date: %date%\" \n \n" +

                            "BanHelp: \"========== %prefix% ==========" +
                            "<newline> /ban <Player> 1,2,3..." +
                            "<newline /ban <Player> <time in Hours> <reson>" +
                            "<newline> /unban <Player>\n" +

                            "NotFoundPlayerData: \"No entry found for this player.\"\n" +
                            "PlayerNotFound: \"Player Not Found\"" +
                            "Playerunban: \"%prefix% You have unban The player %player% \n" +
                            "NOSQLConnectionERROR: \"%prefix% <newlien> Unfortunately we have made a mistake <newline> The conection to the database could not be established.\"\n" +
                            "NoPermission: %prefix% You have no Permissions to use this Command"
                    );
                } catch (IOException e) {
                    logger.error("Could not create config file", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readBanConfig(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException(fileName + " not found");
            }
            Map<String, Object> data = yaml.load(inputStream);

            Map<String, Object> bans = (Map<String, Object>) data.get("Bans");
            Map<String, Object> banlog = (Map<String, Object>) data.get("Banlog");

            bansHost = (String) bans.get("host");
            bansPort = (Integer) bans.get("port");
            bansDatabase = (String) bans.get("database");
            bansCollection = (String) bans.get("collection");
            bansUsername = (String) bans.get("username");
            bansPassword = (String) bans.get("password");

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

    public void readSettingsConfig(String fileName) {
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
            if (inputStream == null) {
                throw new IllegalArgumentException(fileName + " not found");
            }
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