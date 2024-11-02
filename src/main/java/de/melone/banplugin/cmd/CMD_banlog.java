package de.melone.banplugin.cmd;

import com.mojang.brigadier.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.Ban;
import de.melone.banplugin.ulti.Banlog;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CMD_banlog implements SimpleCommand {

    private final ProxyServer proxy;
    private static Player player;
    private static String fromplayer;
    private static String grund;
    private static LocalDateTime datum;
    private static DateTimeFormatter formatter;

    public CMD_banlog(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();


        if (!(source instanceof Player)) {
            System.out.println("This Command is Only for Player");
            return;
        }

        if (!player.hasPermission("Ban.banlog")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + BanPlugin.noperms));
            return;
        }

        if (invocation.arguments().length == 1) {
            String playerName = args[0];

            Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
            Player targetPlayer = optionalPlayer.get();
            if (optionalPlayer.isPresent()) {

                GetPlayerBan(targetPlayer.getUniqueId().toString(), player);
            }
        } else if (invocation.arguments().length == 2) {
            String playerName = args[0];
            Integer count = Integer.valueOf(args[1]);

            Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
            Player targetPlayer = optionalPlayer.get();

            GetPlayerBanCount(targetPlayer.getUniqueId().toString(), player, count);
         } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + " " + BanPlugin.noperms));
        }
    }

    public static void GetPlayerBan(String uuid, Player player) {
        Document query = new Document("_id", uuid);
        Document playerDoc = Banlog.collection.find(query).first();

        if (playerDoc != null) {
            int points = playerDoc.getInteger("Points");
            List<Document> logs = playerDoc.getList("Log", Document.class);
            Document lastBan = logs.get(logs.size() - 1);

            fromplayer = lastBan.getString("From");
            grund = lastBan.getString("Reason");
            Date date = lastBan.getDate("Date");

            LocalDateTime datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.Banlog1)));
            return;
        }
            player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.Banlog1)));
        }
    }

    public static void GetPlayerBanCount(String uuid, Player player, int logIndex) {
        Document query = new Document("_id", uuid);
        Document playerDoc = Banlog.collection.find(query).first();

        if (playerDoc != null) {
            int points = playerDoc.getInteger("Points");
            List<Document> logs = playerDoc.getList("Log", Document.class);

            if (logIndex > 0 && logIndex <= logs.size()) {
                Document selectedBan = logs.get(logIndex - 1);

                fromplayer = selectedBan.getString("From");
                grund = selectedBan.getString("Reason");
                Date date = selectedBan.getDate("Date");

                formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.Banlog2)));
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("Invalid Ban-Index."));
            }
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.NotFound)));
        }
    }

    private static String ConfigMessages(String message) {
        if (message.contains("%logssize%")  || message.contains("%points%") || message.contains("%fromplayer%") || message.contains("%date%")) {
            return message.replace("%spieler%", player.getUsername())
                    .replace("%points%", grund)
                    .replace("%fromplayer%", fromplayer)
                    .replace("%date%", datum.format(formatter))
                    .replace("%prefix%", BanPlugin.prefixMiniMessage);
        }
        return message;
    }
}
