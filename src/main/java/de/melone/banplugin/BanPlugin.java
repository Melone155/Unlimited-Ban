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
import de.melone.banplugin.cmd.CMD_unban;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.BanlogSQL;
import net.kyori.adventure.text.format.TextColor;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
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

    public static String prefix = "§7[" + TextColor.color(255, 165, 0) + "F" + TextColor.color(246, 157, 14) + "u" + TextColor.color(236, 149, 7) + "c" +
            TextColor.color(227, 141, 1) + "h" + TextColor.color(217, 133, 0) + "s" + TextColor.color(207, 125, 0) + "c" +
            TextColor.color(198, 117, 0) + "r" + TextColor.color(188, 109, 0) + "a" + TextColor.color(179, 101, 0) + "f" +
            TextColor.color(169, 93, 0) + "t" + TextColor.color(159, 85, 0) + "." + TextColor.color(149, 77, 0) + "d" +
            TextColor.color(139, 69, 0) + "e" + "§7] ";

    public static String prefixMiniMessage = "<#ffa500>F<#f69d0e>u<#ec9507>c<#e38d01>h<#d98500>s<#cf7d00>c<#c67500>r<#bc6d00>a<#b36500>f<#a95d00>t<#9f5500>.<#954D00>d<#8B4500>e<gray> ";

    private Logger logger;
    private final ProxyServer server;
    private final CommandManager commandManager;
    private ObjectInputFilter.Config config;

    @DataDirectory
    private Path dataDirectory;

    File folder = new File("plugins/Bansystem");
    File file = new File("plugins/Bansystem/MongoDB.yml");

    public static String bansHost = "";
    public static String bansPort = "";
    public static String bansDatabase = "";
    public static String bansUsername = "";
    public static String bansPassword = "";

    public static String banlogHost = "";
    public static String banlogPort = "";
    public static String banlogDatabase = "";
    public static String banlogUsername = "";
    public static String banlogPassword = "";

    @Inject
    public BanPlugin(ProxyServer server, Logger logger, CommandManager commandManager) {
        this.server = server;
        this.logger = logger;
        this.commandManager = commandManager;

        logger.info("Ban Plugin ist Aktive ");

        //BanSQL.ConnectionBan();
        //BanlogSQL.ConnectionBan();

        createConfig();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        server.getEventManager().register(this, new JoinEvent());
        server.getEventManager().register(this, new PlayerChat());

        CommandManager commandManager = server.getCommandManager();
        commandManager.register("ban", new CMD_ban(server));
        commandManager.register("unban", new CMD_unban(server));
    }

    private void createConfig() {

        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // YAML-Daten erstellen
        String bans = "Bans:\n"
                + "  host: localhost\n"
                + "  port: 27017\n"
                + "  database: mydatabase\n"
                + "  username: myuser\n"
                + "  password: mypassword\n";

        String banlog = "Banlog:\n"
                + "  host: localhost\n"
                + "  port: 27017\n"
                + "  database: mydatabase\n"
                + "  username: myuser\n"
                + "  password: mypassword\n";

        // YAML-Datei schreiben
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(bans);
            writer.write(banlog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Readfile() {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            // Daten auslesen
            Map<String, Object> bans = (Map<String, Object>) data.get("Bans");
            String bansHost = (String) bans.get("host");
            String bansPort = (String) bans.get("port");
            String bansDatabase = (String) bans.get("database");
            String bansUsername = (String) bans.get("username");
            String bansPassword = (String) bans.get("password");

            Map<String, Object> banlog = (Map<String, Object>) data.get("Banlog");
            String banlogHost = (String) banlog.get("host");
            String banlogPort = (String) banlog.get("port");
            String banlogDatabase = (String) banlog.get("database");
            String banlogUsername = (String) banlog.get("username");
            String banlogPassword = (String) banlog.get("password");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
