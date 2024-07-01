package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanlogSQL;
import net.kyori.adventure.text.Component;
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

    public CMD_banlog(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (source instanceof Player) {
            player = (Player) source;
            if (invocation.arguments().length == 1) {
                String playerName = args[0];

                Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                Player targetPlayer = optionalPlayer.get();
                if (optionalPlayer.isPresent()) {

                    BanlogSQL.GetPlayerBan(targetPlayer.getUniqueId().toString(), player);
                }
            } else if (invocation.arguments().length == 2) {
                String playerName = args[0];
                Integer count = Integer.valueOf(args[1]);

                Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                Player targetPlayer = optionalPlayer.get();

                BanlogSQL.GetPlayerBanCount(targetPlayer.getUniqueId().toString(), player, count);
            }
        }
    }

    public static void GetPlayerBanCount(String uuid, Player player, int logIndex) {
        Document query = new Document("_id", uuid);
        Document playerDoc = BanlogSQL.collection.find(query).first();

        if (playerDoc != null) {
            int points = playerDoc.getInteger("Punkte");
            List<Document> logs = playerDoc.getList("Log", Document.class);

            if (logIndex > 0 && logIndex <= logs.size()) {
                Document selectedBan = logs.get(logIndex - 1);

                fromplayer = selectedBan.getString("Von");
                grund = selectedBan.getString("Grund");
                Date date = selectedBan.getDate("Datum");

                datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                player.sendMessage(MiniMessage.miniMessage().deserialize(
                        "========== " + BanPlugin.prefixMiniMessage + " ==========" +
                                "<newline> Ban Nummer: " +logIndex +
                                "<newline> Ban:" +
                                "<newline> Grund: " + grund +
                                "<newline> Von: " + fromplayer +
                                "<newline>Datum: " + datum.format(formatter)
                ));
            } else {
                player.sendMessage(Component.text("Ungültiger Ban-Index."));
            }
        } else {
            player.sendMessage(Component.text("Kein Eintrag für diesen Spieler gefunden."));
        }
    }

    public static void GetPlayerBan(String uuid, Player player) {
        Document query = new Document("_id", uuid);
        Document playerDoc = BanlogSQL.collection.find(query).first();

        if (playerDoc != null) {
            int points = playerDoc.getInteger("Punkte");
            List<Document> logs = playerDoc.getList("Log", Document.class);
            Document lastBan = logs.get(logs.size() - 1);

            String fromplayer = lastBan.getString("Von");
            String grund = lastBan.getString("Grund");
            Date date = lastBan.getDate("Datum");

            LocalDateTime datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    "========== " + BanPlugin.prefixMiniMessage + " ==========" +
                            "<newline> Punkte: " + points +
                            "<newline> Anzahl an Bans: " + logs.size() +
                            "<newline> Letzter Ban:" +
                            "<newline> Grund: " + grund +
                            "<newline> Von: " + fromplayer +
                            "<newline>Datum: " + datum.format(formatter)
            ));
        } else {
            player.sendMessage(Component.text("Kein Eintrag für dieen Spieler gefunden."));
        }
    }
}

    private static String ConfigMessages(String message) {
        if (message.contains("%logssize%")  || message.contains("%points%") || message.contains("%fromplayer%") || message.contains("%date%")) {
            return message.replace("%spieler%", player.getUsername())
                    .replace("%points%", reson)
                    .replace("%prefix%", BanPlugin.prefixMiniMessage);
        }
        return message;
    }
}
