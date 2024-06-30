package de.melone.banplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.Listener.JoinEvent;
import de.melone.banplugin.cmd.CMD_ban;
import de.melone.banplugin.cmd.CMD_banlog;
import de.melone.banplugin.cmd.CMD_unban;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.BanlogSQL;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public static String prefixMiniMessage;

    @DataDirectory
    private final Path dataDirectory;

    @Inject
    public BanPlugin(ProxyServer server, Logger logger, CommandManager commandManager, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.commandManager = commandManager;
        this.dataDirectory = dataDirectory;

        createConfig();
        readBanConfig("plugins/Bansystem/MongoDB.yml");
        readSettingsConfig("plugins/Bansystem/Ban.yml");

        BanSQL.ConnectionBan();
        BanlogSQL.ConnectionBan();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new JoinEvent());

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
                            "# All ban times are given in hours \n" +
                            "# Ban type is Chat and Server \n" +
                            "\n" +
                            "Bans: \n" +
                            "  1:\n" +
                            "    Reson: Hacking" +
                            "    time: 1 \n" +
                            "    type: Server \n" +
                            "  2:\n" +
                            "    Reson: AD" +
                            "    time: 1 \n" +
                            "    type: Chat \n" +
                            "  3:\n" +
                            "    Reson: Spam" +
                            "    time: 1 \n" +
                            "    type: Chat \n" +
                            "  4:\n" +
                            "    Reson: Insult" +
                            "    time: 1 \n" +
                            "    type: Chat \n" +
                            "  5:\n" +
                            "    Reson: Bugusing\n" +
                            "    time: 1 \n" +
                            "    type: Server \n" +
                            "  6:\n" +
                            "    Reson: Skin\n" +
                            "    time: 1 \n" +
                            "    type: Server \n" +
                            "  7:\n" +
                            "    Reson: Hatespeech\n" +
                            "    time: 1 \n" +
                            "    type: Chat \n" +
                            "  8:\n" +
                            "    Reson: Illegal buildings\n" +
                            "    time: 1 \n" +
                            "    type: Server \n"
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
                banfile.createNewFile();
                try (FileWriter writer = new FileWriter(banfile)) {
                    writer.write("" +
                            "# Alle This Messages Support MIniMessages \n" +
                            "# https://docs.advntr.dev/index.html" +
                            "\n" +
                            "Prefix: <#ffa500>F<#f69d0e>u<#ec9507>c<#e38d01>h<#d98500>s<#cf7d00>c<#c67500>r<#bc6d00>a<#b36500>f<#a95d00>t<#9f5500>.<#954D00>d<#8B4500>e<gray> \n" +

                            "KickMessage: %prefix% <newline> You have been warned/banned please Join New for more info \n" +

                            "ReturnBan: %prefix% Du hast den Spieler %targetPlayer% vom Server gebannt wegen %reson% \n" +

                            "ReturnChatban: %prefix% Du hast den Spieler %targetPlayer% auf dem Chat gebannt wegen %reson% \n" +

                            "Banlog1: ========== %prefix% ========== " +
                            "<newline>Punkte: %points% \n" +
                            "<newline>Anzahl an Bans: %logssize% \n" +
                            "<newline>Letzter Ban: \n" +
                            "<newline>Grund: %grund% \n" +
                            "<newline>Von: %fromplayer% \n" +
                            "<newline>Datum: %date%" +

                            "Banlog2: ========== %prefix% ==========\n" +
                            "<newline>Ban Nummer: %logIndex% \n" +
                            "<newline>Ban: \n" +
                            "<newline>Grund: %grund% \n" +
                            "<newline>Von: %fromplayer% \n" +
                            "<newline>Datum: %date%" +

                            "NotFound: Kein Eintrag für dieen Spieler gefunden."
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

            Map<String, Object> ban1 = (Map<String, Object>) bans.get("1");
            Map<String, Object> ban2 = (Map<String, Object>) bans.get("2");
            Map<String, Object> ban3 = (Map<String, Object>) bans.get("3");
            Map<String, Object> ban4 = (Map<String, Object>) bans.get("4");
            Map<String, Object> ban5 = (Map<String, Object>) bans.get("5");
            Map<String, Object> ban6 = (Map<String, Object>) bans.get("6");
            Map<String, Object> ban7 = (Map<String, Object>) bans.get("7");
            Map<String, Object> ban8 = (Map<String, Object>) bans.get("8");

            // 1
            reson1 = (String) ban1.get("Reson");
            time1 = (String) ban1.get("time");
            type1 = (String) ban1.get("type");

            // 2
            reson2 = (String) ban2.get("Reson");
            time2 = (String) ban2.get("time");
            type2 = (String) ban2.get("type");

            // 3
            reson3 = (String) ban3.get("Reson");
            time3 = (String) ban3.get("time");
            type3 = (String) ban3.get("type");

            // 4
            reson4 = (String) ban4.get("Reson");
            time4 = (String) ban4.get("time");
            type4 = (String) ban4.get("type");

            // 5
            reson5 = (String) ban5.get("Reson");
            time5 = (String) ban5.get("time");
            type5 = (String) ban5.get("type");

            // 6
            reson6 = (String) ban6.get("Reson");
            time6 = (String) ban6.get("time");
            type6 = (String) ban6.get("type");

            // 7
            reson7 = (String) ban7.get("Reson");
            time7 = (String) ban7.get("time");
            type7 = (String) ban7.get("type");

            // 8
            reson8 = (String) ban8.get("Reson");
            time8 = (String) ban8.get("time");
            type8 = (String) ban8.get("type");

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

            Map<String, Object> bans = (Map<String, Object>) data.get("Bans");
            //Hacking = (Integer) bans.get("Hacking");
            //AD = (Integer) bans.get("Werbung/AD");
            //Spam = (Integer) bans.get("Spam");
            //insult = (Integer) bans.get("Beleidigung/insult");
            //Bugusing = (Integer) bans.get("Bugusing");
            //Skin = (Integer) bans.get("Skin");
            //Hatespeech = (Integer) bans.get("Hatespeech/Diskriminierung/Rassismus");
            //Illegal = (Integer) bans.get("Verbotene Buildings/Illegal buildings");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
