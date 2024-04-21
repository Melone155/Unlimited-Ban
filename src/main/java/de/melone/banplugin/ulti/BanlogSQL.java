package de.melone.banplugin.ulti;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.result.InsertOneResult;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import net.kyori.adventure.text.Component;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class BanlogSQL {

    private static MongoCollection<Document> collection;
    private static MongoClient mongoClient;
    private static String uri = "mongodb://" + BanPlugin.banlogUsername + ":" + BanPlugin.banlogPassword + "@" + BanPlugin.banlogHost + ":" + BanPlugin.banlogPort + "/?authMechanism=SCRAM-SHA-1&authSource=" + BanPlugin.banlogDatabase;

    public static void ConnectionBan() {

        mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("Ban");
        collection = database.getCollection("banlog");

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

    public static void CreatePlayerBan(Player taget, Player player, String reson, LocalDateTime localDateTime) {

        if (!isMongoDBConnected(mongoClient)) {
            player.sendMessage(Component.text(BanPlugin.prefix + "Fehler: 425 Bitte Kontaktieren sie einen Admin"));
        } else {
            System.out.println("Erfolgreich mit der MongoDB-Datenbank verbunden.");

            Document query = new Document("_id", taget.getUniqueId().toString());
            Document existingDoc = collection.find(query).first();

            if (existingDoc == null) {

                Document logEntry = new Document("Von", player.getGameProfile().getName().toString())
                                         .append("Grund", reson)
                                         .append("Datum", localDateTime);

                Document newDocument = new Document("_id", taget.getUniqueId().toString())
                        .append("Punkte", 1)
                        .append("Log", Collections.singletonList(logEntry));
                collection.insertOne(newDocument);
            } else {

                int punkte = existingDoc.getInteger("Punkte");
                int neuePunkte = punkte + 1;

                List<Document> logs = existingDoc.getList("Log", Document.class);
                int nextIndex = logs.size() + 1;


                Document logEntry = new Document("Von", player.getGameProfile().getName().toString())
                        .append("Grund", reson)
                        .append("Datum", localDateTime);

                logs.add(logEntry);
                collection.updateOne(query, new Document("$set", new Document("Punkte", neuePunkte)));
                collection.updateOne(query, new Document("$set", new Document("Log", logs)));

            }
        }
    }
}
