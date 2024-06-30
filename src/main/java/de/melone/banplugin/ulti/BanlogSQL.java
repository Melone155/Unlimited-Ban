package de.melone.banplugin.ulti;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class BanlogSQL {

    private static MongoCollection<Document> collection;
    public static MongoClient mongoClient;
    private static String uri = "mongodb://" + BanPlugin.banlogUsername + ":" + BanPlugin.banlogPassword + "@" + BanPlugin.banlogHost + ":" + BanPlugin.banlogPort + "/?authMechanism=SCRAM-SHA-256";

    public static void ConnectionBan() {

        mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(BanPlugin.banlogDatabase);
        collection = database.getCollection(BanPlugin.banlogCollection);

    }

    public static boolean isMongoDBConnected(MongoClient mongoClient) {
        try {
            mongoClient.listDatabaseNames();

            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayerBan(Player target, Player player, String reason, LocalDateTime localDateTime) {
        if (!isMongoDBConnected(mongoClient)) {
            player.sendMessage(Component.text(BanPlugin.prefixMiniMessage + "Fehler: 425 Bitte Kontaktieren sie einen Admin"));
        } else {
            try {
                Document query = new Document("_id", target.getUniqueId().toString());
                Document existingDoc = collection.find(query).first();

                if (existingDoc == null) {
                    Document logEntry = new Document("Von", player.getGameProfile().getName().toString())
                            .append("Grund", reason)
                            .append("Datum", localDateTime);

                    Document newDocument = new Document("_id", target.getUniqueId().toString())
                            .append("Punkte", 1)
                            .append("Log", Collections.singletonList(logEntry));
                    collection.insertOne(newDocument);
                } else {
                    Integer punkte = existingDoc.getInteger("Punkte");
                    int neuePunkte = (punkte != null) ? punkte + 1 : 1; // Fallback to 1 if "Punkte" is null

                    List<Document> logs = existingDoc.getList("Log", Document.class);
                    if (logs == null) {
                        logs = new ArrayList<>();
                    }

                    Document logEntry = new Document("Von", player.getGameProfile().getName().toString())
                            .append("Grund", reason)
                            .append("Datum", localDateTime);

                    logs.add(logEntry);
                    collection.updateOne(query, new Document("$set", new Document("Punkte", neuePunkte)));
                    collection.updateOne(query, new Document("$set", new Document("Log", logs)));
                }
            } catch (Exception e) {
                System.out.println("Error when creating or updating the entry: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void GetPlayerBan(String uuid, Player player) {
        Document query = new Document("_id", uuid);
        Document playerDoc = collection.find(query).first();

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

    public static void GetPlayerBanCount(String uuid, Player player, int logIndex) {
        Document query = new Document("_id", uuid);
        Document playerDoc = collection.find(query).first();

        if (playerDoc != null) {
            int points = playerDoc.getInteger("Punkte");
            List<Document> logs = playerDoc.getList("Log", Document.class);

            if (logIndex > 0 && logIndex <= logs.size()) {
                Document selectedBan = logs.get(logIndex - 1);

                String fromplayer = selectedBan.getString("Von");
                String grund = selectedBan.getString("Grund");
                Date date = selectedBan.getDate("Datum");

                LocalDateTime datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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

    private static String ConfigMessages(String message, String playerName) {
        if (message.contains("%logssize%") || message.contains("%targetPlayer%") || message.contains("%points%") || message.contains("%fromplayer%")) {
            return message.replace("%spieler%", playerName);
        }
        return message;
    }
}
