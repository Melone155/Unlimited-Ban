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

    public static MongoCollection<Document> collection;
    public static MongoClient mongoClient;
    private static String uri = "mongodb://" + BanPlugin.banlogUsername + ":" + BanPlugin.banlogPassword + "@" + BanPlugin.banlogHost + ":" + BanPlugin.banlogPort + "/Ban?authSource=Ban";

    public static void ConnectionBan() {

        mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(BanPlugin.banlogDatabase);
        collection = database.getCollection(BanPlugin.banlogCollection);
    }

    public static boolean isMongoDBConnected(MongoClient mongoClient) {
        try {
            mongoClient.listDatabaseNames();

            System.out.println("Hallo World");

            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayerBan(Player target, Player player, String reason, LocalDateTime localDateTime) {
        if (!isMongoDBConnected(mongoClient)) {
            player.sendMessage(Component.text(BanPlugin.prefixMiniMessage + "Fehler: 425 Please contact an Admin"));
        } else {
            try {
                Document query = new Document("_id", target.getUniqueId().toString());
                Document existingDoc = collection.find(query).first();

                if (existingDoc == null) {
                    Document logEntry = new Document("From", player.getGameProfile().getName().toString())
                            .append("Reason", reason)
                            .append("Date", localDateTime);

                    Document newDocument = new Document("_id", target.getUniqueId().toString())
                            .append("Points", 1)
                            .append("Log", Collections.singletonList(logEntry));
                    collection.insertOne(newDocument);
                } else {
                    Integer punkte = existingDoc.getInteger("Points");
                    int neuePunkte = (punkte != null) ? punkte + 1 : 1; // Fallback to 1 if "Punkte" is null

                    List<Document> logs = existingDoc.getList("Log", Document.class);
                    if (logs == null) {
                        logs = new ArrayList<>();
                    }

                    Document logEntry = new Document("From", player.getGameProfile().getName().toString())
                            .append("Reason", reason)
                            .append("Date", localDateTime);

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
}
