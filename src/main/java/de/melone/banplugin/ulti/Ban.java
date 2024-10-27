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
import java.util.*;

public class Ban {

    public static MongoCollection<org.bson.Document> collection;
    public static MongoClient mongoClient;
    private static final String uri = "mongodb://" + BanPlugin.banlogUsername + ":" + BanPlugin.banlogPassword + "@" + BanPlugin.banlogHost + ":" + BanPlugin.banlogPort + "/Ban?authSource=" + BanPlugin.banlogDatabase;

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

    public static void AddBanLog(Player player,Player target ,String reason, LocalDateTime localDateTime){
        if (!isMongoDBConnected(mongoClient)) {
            player.sendMessage(Component.text(BanPlugin.prefixMiniMessage + "Fehler: 425 Please contact an Admin"));
            return;
        }

        try {
            Document query = new Document("_id", target.getUniqueId().toString());
            Document existingDoc = collection.find(query).first();

            if (existingDoc == null) {
                CreatePlayerlog(player, target, reason, localDateTime);
                return;
            }

            Integer points = existingDoc.getInteger("Points");
            int newpoints = (points != null) ? points + 1 : 1; // Fallback to 1 if "Punkte" is null

            List<Document> logs = existingDoc.getList("Log", Document.class);
            if (logs == null) {
                logs = new ArrayList<>();
            }

            Document logEntry = new Document("From", player.getGameProfile().getName())
                    .append("Reason", reason)
                    .append("Date", localDateTime);

            logs.add(logEntry);
            collection.updateOne(query, new Document("$set", new Document("Points", newpoints)));
            collection.updateOne(query, new Document("$set", new Document("Log", logs)));

        } catch (Exception e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + "Es ist ein Fehler aufgetreten"));
            System.out.println("Error when creating or updating the entry: " + e.getMessage());
        }
    }

    private static void CreatePlayerlog(Player player,Player target ,String reason, LocalDateTime localDateTime){
        Document logEntry = new Document("From", player.getGameProfile().getName())
                .append("Reason", reason)
                .append("Date", localDateTime);

        Document newDocument = new Document("_id", target.getUniqueId().toString())
                .append("Points", 1)
                .append("Log", Collections.singletonList(logEntry));
        collection.insertOne(newDocument);
    }
}
