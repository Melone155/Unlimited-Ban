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

    public static int Hacking;
    public static int AD;
    public static int Spam;
    public static int insult;
    public static int Bugusing;
    public static int Skin;
    public static int Hatespeech;
    public static int Illegal;

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
                    writer.write("# All ban times are given in hours \n" +
                            "Prefix: <#ffa500>F<#f69d0e>u<#ec9507>c<#e38d01>h<#d98500>s<#cf7d00>c<#c67500>r<#bc6d00>a<#b36500>f<#a95d00>t<#9f5500>.<#954D00>d<#8B4500>e<gray> \n" +
                            "time: \n" +
                            "  Hacking: " + 1 + "\n" +
                            "  Werbung/AD: " + 1 + "\n" +
                            "  Spam: " + 1 + "\n" +
                            "  Beleidigung/insult: " + 1 + "\n" +
                            "  Bugusing: " + 1 + "\n" +
                            "  Skin: " + 1 + "\n" +
                            "  Hatespeech/Diskriminierung/Rassismus: " + 1 + "\n" +
                            "  Verbotene Buildings/Illegal buildings: "  + 1 + "\n");
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
            if (inputStream == null) {
                throw new IllegalArgumentException(fileName + " not found");
            }
            Map<String, Object> data = yaml.load(inputStream);

            prefixMiniMessage = (String) data.get("Prefix");

            Map<String, Object> bans = (Map<String, Object>) data.get("time");
            Hacking = (Integer) bans.get("Hacking");
            AD = (Integer) bans.get("Werbung/AD");
            Spam = (Integer) bans.get("Spam");
            insult = (Integer) bans.get("Beleidigung/insult");
            Bugusing = (Integer) bans.get("Bugusing");
            Skin = (Integer) bans.get("Skin");
            Hatespeech = (Integer) bans.get("Hatespeech/Diskriminierung/Rassismus");
            Illegal = (Integer) bans.get("Verbotene Buildings/Illegal buildings");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
