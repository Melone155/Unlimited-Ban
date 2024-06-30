package de.melone.banplugin.ulti;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import net.kyori.adventure.text.Component;
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
            System.out.println("Erfolgreich mit der MongoDB-Datenbank verbunden.");

            try {
                Document query = new Document("_id", target.getUniqueId().toString());
                Document existingDoc = collection.find(query).first();

                if (existingDoc == null) {
                    System.out.println("Kein bestehender Eintrag gefunden. Erstelle einen neuen Eintrag.");
                    Document logEntry = new Document("Von", player.getGameProfile().getName().toString())
                            .append("Grund", reason)
                            .append("Datum", localDateTime);

                    Document newDocument = new Document("_id", target.getUniqueId().toString())
                            .append("Punkte", 1)
                            .append("Log", Collections.singletonList(logEntry));
                    collection.insertOne(newDocument);
                    System.out.println("Neuer Eintrag erfolgreich erstellt.");
                } else {
                    System.out.println("Bestehender Eintrag gefunden. Aktualisiere den Eintrag.");
                    Integer punkte = existingDoc.getInteger("Punkte");
                    int neuePunkte = (punkte != null) ? punkte + 1 : 1; // Fallback to 1 if "Punkte" is null

                    List<Document> logs = existingDoc.getList("Log", Document.class);
                    if (logs == null) {
                        logs = new ArrayList<>(); // Initialize logs if null
                    }

                    Document logEntry = new Document("Von", player.getGameProfile().getName().toString())
                            .append("Grund", reason)
                            .append("Datum", localDateTime);

                    logs.add(logEntry);
                    collection.updateOne(query, new Document("$set", new Document("Punkte", neuePunkte)));
                    collection.updateOne(query, new Document("$set", new Document("Log", logs)));
                    System.out.println("Eintrag erfolgreich aktualisiert.");
                }
            } catch (Exception e) {
                System.out.println("Fehler beim Erstellen oder Aktualisieren des Eintrags: " + e.getMessage());
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

            String von = lastBan.getString("Von");
            String grund = lastBan.getString("Grund");
            Date date = lastBan.getDate("Datum");

            LocalDateTime datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            player.sendMessage(Component.text("Punkte: " + points));
            player.sendMessage(Component.text("Anzahl an Bans: " + logs.size()));
            player.sendMessage(Component.text("Letzter Ban:"));
            player.sendMessage(Component.text("Grund: " + grund));
            player.sendMessage(Component.text("Von: " + von));
            player.sendMessage(Component.text("Datum: " + datum.format(formatter)));
        } else {
            player.sendMessage(Component.text("Kein Eintrag für Spieler mit ID " + uuid + " gefunden."));
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

                String von = selectedBan.getString("Von");
                String grund = selectedBan.getString("Grund");
                Date date = selectedBan.getDate("Datum");

                LocalDateTime datum = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                player.sendMessage(Component.text("Ban Nummer: " + logIndex));
                player.sendMessage(Component.text("Grund: " + grund));
                player.sendMessage(Component.text("Von: " + von));
                player.sendMessage(Component.text("Datum: " + datum.format(formatter)));
            } else {
                player.sendMessage(Component.text("Ungültiger Ban-Index."));
            }
        } else {
            player.sendMessage(Component.text("Kein Eintrag für Spieler mit ID " + uuid + " gefunden."));
        }
    }
}
